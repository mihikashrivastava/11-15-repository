package com.purdue;

import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.mockito.Mockito.*;
import javax.swing.*;

class MainClientTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testNewUserSignup() {
        // Mock JOptionPane to simulate user inputs and interactions
        when(JOptionPane.showConfirmDialog(null, "Are you a new user?", "Login or Signup", JOptionPane.YES_NO_OPTION))
            .thenReturn(JOptionPane.YES_OPTION); // Simulate user selecting "Yes, I am a new user."

        when(JOptionPane.showInputDialog("Enter username:")).thenReturn("newUser");
        when(JOptionPane.showInputDialog("Enter password:")).thenReturn("password123");
        when(JOptionPane.showInputDialog("Enter email:")).thenReturn("email@example.com");
        when(JOptionPane.showInputDialog("Enter first name:")).thenReturn("John");
        when(JOptionPane.showInputDialog("Enter last name:")).thenReturn("Doe");

        // Assuming the response after all valid inputs
        when(JOptionPane.showInputDialog(null, "Enter the command:"))
            .thenReturn("exit");

        // Mock further interactions as needed for test completeness
        // ...

        // Call the actual method that triggers these interactions
        MainClient.main(new String[]{});

        // Verify that all inputs were consumed and processed correctly
        verify(JOptionPane.times(1)).showMessageDialog(null, "Account created successfully!");
    }

    @Test
    void testExistingUserLogin() {
        when(JOptionPane.showConfirmDialog(null, "Are you a new user?", "Login or Signup", JOptionPane.YES_NO_OPTION))
            .thenReturn(JOptionPane.NO_OPTION); // Simulate user selecting "No, I am an existing user."

        when(JOptionPane.showInputDialog("Enter username:")).thenReturn("existingUser");
        when(JOptionPane.showInputDialog("Enter password:")).thenReturn("correctPassword");

        // Assuming the response after valid login
        when(JOptionPane.showInputDialog(null, "Enter the command:"))
            .thenReturn("exit");

        MainClient.main(new String[]{});

        // Verify correct login handling
        verify(JOptionPane.times(1)).showMessageDialog(null, "Logged in successfully!");
    }
}
