package com.purdue;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class MessageManager implements MessageManagerService {

    public synchronized void sendMessage(User to, User from, Message message) {
        String fileName = "";
        if (to.getUsername().compareTo(from.getUsername()) < 0) {
            fileName = String.format("%s_%s.txt", to.getUsername(), from.getUsername());
        } else {
            fileName = String.format("%s_%s.txt", from.getUsername(), to.getUsername());
        }

        File f = new File(fileName);
        if (f.exists()) {
            var messages = new ArrayList<Message>();
            try(var ois = new ObjectInputStream(new FileInputStream(f))) {
                messages = (ArrayList<Message>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messages.add(message);
            try(var oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(messages);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            var messages = new ArrayList<Message>();
            messages.add(message);
            try(var oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(messages);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Message createMessageObject(String message, boolean isImage, User sender, User receiver, LocalDateTime timestamp, byte[] image) {
        return new Message(message, isImage, sender, receiver, timestamp, image);
    }

    //checks to see if the user exists and if the user is not already blocked. If both conditions are satisfied, then userTo gets added to userFrom's blocked array.
    public synchronized boolean block(User userFrom, User userTo) {
        if (!search(userFrom) || !search(userTo)) {
            return false;
        }
        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        users.remove(userTo);
        users.remove(userFrom);

        if (userFrom.getBlocked().contains(userTo)) {
            return true;
        } else {
            userFrom.addBlocked(userTo);
            users.add(userTo);
            users.add(userFrom);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true; // User successfully blocked
        }
    }

    //checks to see if the user exists, and if the user is already blocked. If both of these conditions are satisfied, then removed userTo from userFrom's blocked array.
    public synchronized boolean unblock(User userFrom, User userTo) throws UserNotFoundException {

        if (!search(userFrom) || !search(userTo)) {
            return false;
        }
        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        users.remove(userFrom);
        users.remove(userTo);
        // Check if the user is actually blocked
        if (userFrom.getBlocked().contains(userTo)) {
            // Remove the user from the blocked list
            userFrom.removeBlocked(userTo);
            users.add(userFrom);
            users.add(userTo);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true; // Successfully unblocked
        } else {
            return false; // User was not blocked
        }
    }

    // checks if the userTo is friends with userFrom, and if ture, then userTo will be removed from userFrom's friends array.
    public synchronized boolean removeFriend(User userFrom, User userTo) {

        if (!search(userFrom) || !search(userTo)) {
            return false;
        }
        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        users.remove(userFrom);
        users.remove(userTo);

        if (userTo.getFriends().contains(userFrom)) {
            userTo.getFriends().remove(userFrom);
            userFrom.getFriends().remove(userTo);
            users.add(userFrom);
            users.add(userTo);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean search(User user) {

        ArrayList<User> users = new ArrayList<User>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (users.contains(user)) {
            return true;
        } else {
            return false;
        }
    }

    // checks to see if the user is blocked, already a friend, or has already been requested. If not, the userTo gets added to userFrom's requestedFriends arraylist, and userFrom gets added to userTo's pending friend request array.
    public synchronized boolean sendFriendRequest(User userFrom, User userTo) throws UserNotFoundException {

        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        users.remove(userFrom);
        users.remove(userTo);

        if (userFrom.getBlocked().contains(userTo)) {
            return false;
        } if (userFrom.getFriends().contains(userTo)) {
            return false;
        } if (userFrom.getRequestedFriends().contains(userTo)) {
            return false;
        }
        else {

            userFrom.getRequestedFriends().add(userTo);
            userTo.getPendingFriendRequests().add(userFrom);

            users.add(userFrom);
            users.add(userTo);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    // checks to see if the user is blocked, and if not, then userTo gets removed from userFrom's requestedFriends array and added to the friends array, and userFrom get removed from userTo's pendingFriendRequest array, and added to userTo's friends array.
    public synchronized boolean acceptFriendRequest(User userFrom, User userTo) throws UserNotFoundException {

        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        users.remove(userFrom);
        users.remove(userTo);

        if (userTo.getBlocked().contains(userFrom)) {
            return false;
        } else {
            userFrom.getRequestedFriends().remove(userTo);
            userTo.getPendingFriendRequests().remove(userFrom);
            userFrom.getFriends().add(userTo);
            userTo.getFriends().add(userFrom);


            users.add(userFrom);
            users.add(userTo);


            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        }
    }

    // checks to see if the user is blocked, then removes userTo from userFrom's requestedFriends array, and removed userFrom from userTo's pendingFriendRequests array
    public synchronized boolean rejectFriendRequest(User userFrom, User userTo) throws UserNotFoundException {

        if (!search(userFrom) || !search(userTo)) {
            return false;
        }

        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        users.remove(userFrom);
        users.remove(userTo);

        if (userTo.getBlocked().contains(userFrom)) {
            return false;
        } else {
            userFrom.getRequestedFriends().remove(userTo);
            userTo.getPendingFriendRequests().remove(userFrom);

            users.add(userFrom);
            users.add(userTo);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
                oos.writeObject(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    //Checks the users file to see which users username stars with s, if it does, returns an arraylist of the users to display for the search feature
    public ArrayList<User> searchUsersFromFile(String s) {

        ArrayList<User> users = new ArrayList<>();
        ArrayList<User> toReturn = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        for (User user : users) {
            if (user.getUsername().startsWith(s)) {
                toReturn.add(user);
            }
        }

        return toReturn;
    }

}
