package com.purdue;
import java.io.*;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        File userFile = new File("users.bin");
        var users = new ArrayList<User>();
        try (var ois = new ObjectInputStream(new FileInputStream(userFile))) {
            users = (ArrayList<User>) ois.readObject();
            for (User user : users) {
                if (user.getUsername().equals("john123")){
                    for (User blockedUser : user.getBlocked()) {
                        System.out.println(blockedUser.getUsername());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
