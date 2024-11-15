package com.purdue.socialmedia.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import com.purdue.socialmedia.data.User;
import com.purdue.socialmedia.data.Message;
import com.purdue.socialmedia.data.MessageManager;
import com.purdue.socialmedia.exeptions.UserNotFoundException;

public class MessageManagerTest {

    private MessageManager messageManager;
    private User luffy;
    private User zoro;
    private Message message;

    @Before
    public void setUp() {
        messageManager = new MessageManager();
        luffy = new User("luffy", "pirateking", "luffy@onepiece.com", "Monkey D.", "Luffy", "luffy.jpg");
        zoro = new User("zoro", "swordsman", "zoro@onepiece.com", "Roronoa", "Zoro", "zoro.jpg");
        message = messageManager.createMessageObject("Hello, Zoro!", false, luffy, zoro, LocalDateTime.now(), null);

        // Clear or reset the users.bin file before each test
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            ArrayList<User> users = new ArrayList<User>();
            users.add(luffy);
            users.add(zoro);
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test for sendMessage
    @Test
    public void testSendMessage() {
        messageManager.sendMessage(zoro, luffy, message);
        File file = new File("luffy_zoro.txt");
        assertTrue("Message file should be created", file.exists());
    }

    // Test for createMessageObject
    @Test
    public void testCreateMessageObject() {
        assertNotNull("Message should not be null", message);
        assertEquals("Message content should match", "Hello, Zoro!", message.getMessage());
        assertEquals("Sender should be Luffy", luffy, message.getSender());
        assertEquals("Receiver should be Zoro", zoro, message.getReceiver());
    }

    // Test for Block method
    @Test
    public void testBlockUser() throws UserNotFoundException {

        assertFalse("User Zoro should not be blocked initially", luffy.getBlocked().contains(zoro));
        messageManager.block(luffy, zoro);
        assertTrue("User Zoro should be blocked by Luffy", luffy.getBlocked().contains(zoro));
    }

    // Test for unblock method
    @Test
    public void testUnblockUser() throws UserNotFoundException {
        messageManager.block(luffy, zoro);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            ArrayList<User> users = new ArrayList<>();
            users.add(zoro);
            users.add(luffy);
            oos.writeObject(users);
        } catch (IOException e) {
            fail("Setup failed: Unable to write to users.bin");
        }

        assertTrue("User Zoro should be blocked by Luffy", luffy.getBlocked().contains(zoro));
        messageManager.unblock(luffy, zoro);
        assertFalse("User Zoro should be unblocked by Luffy", luffy.getBlocked().contains(zoro));
    }

    // Test for removeFriend method
    @Test
    public void testRemoveFriend() {
        luffy.addFriend(zoro);
        zoro.addFriend(luffy);
        assertTrue("Luffy and Zoro should be friends initially", luffy.getFriends().contains(zoro));
        messageManager.removeFriend(luffy, zoro);
        assertFalse("Luffy and Zoro should no longer be friends", luffy.getFriends().contains(zoro));
    }

    // Test for search method
    @Test
    public void testSearchUserExists() {
        assertTrue("User Luffy should be found", messageManager.search(luffy));
    }

    @Test
    public void testSearchUserNotFound() {
        User tom = new User("tom123", "hellothere", "tom123@gmail.com", "Tom", "Cat", "tom.jpg");
        assertFalse("User tom should not be found", messageManager.search(tom)); // Assuming tom is not in users.bin
    }

    // Test for sendFriendRequest method
    @Test
    public void testSendFriendRequest() throws UserNotFoundException {
        var users = new ArrayList<User>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        users.add(zoro);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            fail("Setup failed: Unable to write to users.bin");
        }

        assertTrue("Friend request should be sent successfully", messageManager.sendFriendRequest(luffy, zoro));
        assertTrue("Zoro should be in Luffy's requested friends list", luffy.getRequestedFriends().contains(zoro));
        assertTrue("Luffy should be in Zoro's pending friend requests list", zoro.getPendingFriendRequests().contains(luffy));
    }

    // Test for acceptFriendRequest method
    @Test
    public void testAcceptFriendRequest() throws UserNotFoundException {
        luffy.getRequestedFriends().add(zoro);
        zoro.getPendingFriendRequests().add(luffy);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            ArrayList<User> users = new ArrayList<>();
            users.add(luffy);
            users.add(zoro);
            oos.writeObject(users);
        } catch (IOException e) {
            fail("Setup failed: Unable to write to users.bin");
        }

        assertTrue("Friend request should be accepted successfully", messageManager.acceptFriendRequest(luffy, zoro));
        assertTrue("Luffy and Zoro should be friends", luffy.getFriends().contains(zoro));
        assertTrue("Zoro and Luffy should be friends", zoro.getFriends().contains(luffy));
    }

    // Test for rejectFriendRequest method
    @Test
    public void testRejectFriendRequest() throws UserNotFoundException {
        luffy.getRequestedFriends().add(zoro);
        zoro.getPendingFriendRequests().add(luffy);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            ArrayList<User> users = new ArrayList<>();
            users.add(luffy);
            users.add(zoro);
            oos.writeObject(users);
        } catch (IOException e) {
            fail("Setup failed: Unable to write to users.bin");
        }

        assertTrue("Friend request should be rejected successfully", messageManager.rejectFriendRequest(luffy, zoro));
        assertFalse("Zoro should not be in Luffy's requested friends list", luffy.getRequestedFriends().contains(zoro));
        assertFalse("Luffy should not be in Zoro's pending friend requests list", zoro.getPendingFriendRequests().contains(luffy));
    }



}
