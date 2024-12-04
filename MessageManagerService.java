package com.purdue;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface MessageManagerService {
    public void sendMessage(User to, User from, Message message);
    public Message createMessageObject(String message, boolean isImage, User sender, User receiver, LocalDateTime timestamp, byte[] image);
    public boolean block(User userFrom, User userTo);
    public boolean unblock(User userFrom, User userTo) throws UserNotFoundException;
    public boolean removeFriend(User userFrom, User userTo);
    public boolean search(User user);
    public boolean sendFriendRequest(User userFrom, User userTo) throws UserNotFoundException;
    public boolean acceptFriendRequest(User userFrom, User userTo) throws UserNotFoundException;
    public boolean rejectFriendRequest(User userFrom, User userTo) throws UserNotFoundException;
    public ArrayList<User> searchUsersFromFile(String s);
    public ArrayList<User> readUsersFromFile();
    private void updateUsersInFile(ArrayList<User> users, User... updatedUsers);
    public static String getCorrectFileName(String user1, String user2);
    public static ArrayList<Message> getMessagesFromFile(String fileName);
    public void deleteMessage(String fileName, String messageId);
}
