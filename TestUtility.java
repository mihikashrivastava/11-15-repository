package com.purdue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtility {

    //makes 20 new people!
    public static void createTestData() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            users.add(new User("user" + i, "pass" + i, "email" + i + "@example.com", "First" + i, "Last" + i, "image" + i));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearTestData() {
        new File("users.bin").delete();
    }

    //makes random username
    public static String getRandomUsername() {
        Random rand = new Random();
        return "user" + (rand.nextInt(20) + 1);  // returns a username like "user15"
    }
}
