package com.purdue;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MessageManager implements MessageManagerService {

    @Override
    public synchronized void sendMessage(User to, User from, Message message) {
        String fileName = to.getUsername().compareTo(from.getUsername()) < 0
                ? String.format("%s_%s.bin", to.getUsername(), from.getUsername())
                : String.format("%s_%s.bin", from.getUsername(), to.getUsername());

        File file = new File(fileName);
        ArrayList<Message> messages = new ArrayList<>();

        if (file.exists()) {
            try (var ois = new ObjectInputStream(new FileInputStream(file))) {
                messages = (ArrayList<Message>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        messages.add(message);

        try (var oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message createMessageObject(String message, boolean isImage, User sender, User receiver,
                                       LocalDateTime timestamp, byte[] image) {
        return new Message(message, isImage, sender, receiver, timestamp, image);
    }

    @Override
    public synchronized boolean block(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userFrom.getBlocked().contains(userTo)) {
            return true;
        }

        userFrom.addBlocked(userTo);
        updateUsersInFile(users, userFrom, userTo);
        return true;
    }

    @Override
    public synchronized boolean unblock(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userFrom.getBlocked().contains(userTo)) {
            userFrom.removeBlocked(userTo);
            updateUsersInFile(users, userFrom, userTo);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean removeFriend(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userTo.getFriends().contains(userFrom)) {
            userTo.getFriends().remove(userFrom);
            userFrom.getFriends().remove(userTo);
            updateUsersInFile(users, userFrom, userTo);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean search(User user) {
        ArrayList<User> users = readUsersFromFile();
        return users.contains(user);
    }

    @Override
    public synchronized boolean sendFriendRequest(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userFrom.getBlocked().contains(userTo) ||
                userFrom.getFriends().contains(userTo) ||
                userFrom.getRequestedFriends().contains(userTo)) {
            return false;
        }

        userFrom.getRequestedFriends().add(userTo);
        userTo.getPendingFriendRequests().add(userFrom);
        updateUsersInFile(users, userFrom, userTo);
        return true;
    }

    @Override
    public synchronized boolean acceptFriendRequest(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userTo.getBlocked().contains(userFrom)) {
            return false;
        }

        userFrom.getRequestedFriends().remove(userTo);
        userTo.getPendingFriendRequests().remove(userFrom);
        userFrom.getFriends().add(userTo);
        userTo.getFriends().add(userFrom);
        updateUsersInFile(users, userFrom, userTo);
        return true;
    }

    @Override
    public synchronized boolean rejectFriendRequest(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = readUsersFromFile();

        if (userTo.getBlocked().contains(userFrom)) {
            return false;
        }

        userFrom.getRequestedFriends().remove(userTo);
        userTo.getPendingFriendRequests().remove(userFrom);
        updateUsersInFile(users, userFrom, userTo);
        return true;
    }

    @Override
    public ArrayList<User> searchUsersFromFile(String s) {
        ArrayList<User> users = readUsersFromFile();
        ArrayList<User> matchedUsers = new ArrayList<>();

        for (User user : users) {
            if (user.getUsername().startsWith(s)) {
                matchedUsers.add(user);
            }
        }
        return matchedUsers;
    }

    private ArrayList<User> readUsersFromFile() {
        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    private void updateUsersInFile(ArrayList<User> users, User... updatedUsers) {
        for (User updatedUser : updatedUsers) {
            users.removeIf(u -> u.getUsername().equals(updatedUser.getUsername()));
            users.add(updatedUser);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
