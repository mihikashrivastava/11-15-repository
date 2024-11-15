package com.purdue;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainServer {
    public static void main(String[] args) {
        // Manager to handle user messaging and related functionalities
        MessageManager messageManager = new MessageManager();

        // Try-with-resources block to create a ServerSocket and handle client connections
        try (ServerSocket serverSocket = new ServerSocket(8080)) { // Server listens on port 8080
            System.out.println("Server is running on port 8080...");

            while (true) { // Infinite loop to handle multiple client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                // Create a new thread for each client connection
                ClientHandler clientHandler = new ClientHandler(clientSocket, messageManager);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) { // Handle exceptions for server operations
            e.printStackTrace();
        }
    }

    // Method to search for a user by username in a serialized file
    public static User searchUserFromUsername(String username) throws UserNotFoundException {
        // Try-with-resources to read the serialized users data
        try (var ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            // Deserialize the list of users
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            // Search for the user with the matching username
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return user; // Return the user if found
                }
            }
        } catch (Exception e) { // Catch any exceptions during file I/O or deserialization
            e.printStackTrace();
        }
        // Throw exception if the user is not found
        throw new UserNotFoundException("User not found");
    }

    // Method to process client commands and interact with the MessageManager
    public static String processCommand(String inputFromClient, MessageManager messageManager) {
        // Split the client input into parts (e.g., command and arguments)
        String[] inputArr = inputFromClient.split(",");
        String outputToClient = "";

        // Process the command based on the first part of the input
        try {
            switch (inputArr[0].toLowerCase()) {
                case "block": // Block a user
                    messageManager.block(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your block command has succeeded";
                    break;
                case "unblock": // Unblock a user
                    messageManager.unblock(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your unblock command has succeeded";
                    break;
                case "remove friend": // Remove a user from friend list
                case "remove": // Alternative command syntax for removing a friend
                    messageManager.removeFriend(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your remove command has succeeded";
                    break;
                case "add friend": // Add a user as a friend
                case "add": // Alternative command syntax for adding a friend
                    messageManager.sendFriendRequest(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your friend command has succeeded";
                    break;
                case "accept friend": // Accept a friend request
                    messageManager.acceptFriendRequest(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your friend command has succeeded";
                    break;
                case "reject friend": // Reject a friend request
                    messageManager.rejectFriendRequest(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]));
                    outputToClient = "Your friend command has succeeded";
                    break;
                case "send message":
                    messageManager.sendMessage(MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]), messageManager.createMessageObject(inputArr[3], false, MainServer.searchUserFromUsername(inputArr[1]), MainServer.searchUserFromUsername(inputArr[2]), LocalDateTime.now(), null));
                    outputToClient = "Your message has succeeded";
                    break;
                default: // Handle unknown commands
                    outputToClient = "Unknown command";
            }
        } catch (UserNotFoundException e) { // Handle case when a user is not found
            outputToClient = "User not found, Command Did not Succeed";
        }

        return outputToClient; // Return the response to the client
    }
}
