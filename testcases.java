package com.purdue;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class testcases {

    LoginSignUp loginSignUp = new LoginSignUp();
    MessageManager messageManager = new MessageManager();
    @Test
    @DisplayName("LoginSignUp: Valid Login")
    public void testValidLogin() throws Exception {
        loginSignUp.createNewUser("tom", "thomas123", "tom@purdue.com", "Tom", "Cat", "tom.jpg");
        loginSignUp.createNewUser("jerry", "jerry123", "jerry@purdue.com", "Jerry", "Mouse", "tom.jpg");

        User tom = MainServer.searchUserFromUsername("tom");
        User jerry = MainServer.searchUserFromUsername("jerry");
        String toSend = "block,jerry,tom"; //This is so that Tom is blocking jerry.
        MainServer.processCommand(toSend, messageManager);
        assertTrue(jerry.getBlocked().contains(tom));
    }

}
