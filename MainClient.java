package.com.purdue;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MainClient implements MainClientService {


    public static void main(String[] args) {
        // The following try-catch is creating the socket, bufferedreader,
        // and printwriter while catching an IOException
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

            // login

            JOptionPane.showMessageDialog(null, "Welcome to Purdue Social Media!");
            int choice = JOptionPane.showConfirmDialog(null, "Are you a new user?", "Login or Signup", JOptionPane.YES_NO_OPTION);
            boolean newUser = (choice == JOptionPane.YES_OPTION);

            String username = "";
            String password = "";
            String email = "";
            String firstName = "";
            String lastName = "";
            String profileImage = "default";
            LoginSignUp loginSignUP = new LoginSignUp();

            String loggedInUsername = "";

            if (newUser) {
                // Loop for username
                boolean validUsername = false;
                while (!validUsername) {
                    username = JOptionPane.showInputDialog("Enter username:");
                    if (username.contains(",")) {
                        JOptionPane.showMessageDialog(null, "Username cannot contain commas.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Check if username already exists
                    boolean usernameExists = false;
                    List<String> accountList = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            accountList.add(line);
                        }
                        for (String account : accountList) {
                            String[] parts = account.split(",");
                            String existingUsername = parts[0];
                            if (existingUsername.equals(username)) {
                                usernameExists = true;
                                JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.", "Username Taken", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!usernameExists) {
                        validUsername = true;
                    }
                }

                // Loop for password
                boolean validPassword = false;
                while (!validPassword) {
                    password = JOptionPane.showInputDialog("Enter password:");
                    if (password.contains(",")) {
                        JOptionPane.showMessageDialog(null, "Password cannot contain commas.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    } else if (password.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                    } else {
                        validPassword = true;
                    }
                }

                // Loop for email
                boolean validEmail = false;
                while (!validEmail) {
                    email = JOptionPane.showInputDialog("Enter email:");
                    if (!email.contains("@")) {
                        JOptionPane.showMessageDialog(null, "Invalid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                    } else {
                        validEmail = true;
                    }
                }

                // Loop for first name
                while (firstName.isEmpty()) {
                    firstName = JOptionPane.showInputDialog("Enter first name:");
                    if (firstName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "First name cannot be empty.", "Invalid First Name", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // Loop for last name
                while (lastName.isEmpty()) {
                    lastName = JOptionPane.showInputDialog("Enter last name:");
                    if (lastName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Last name cannot be empty.", "Invalid Last Name", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // Attempt to create the user
                try {
                    loginSignUP.createNewUser(username, password, email, firstName, lastName, profileImage);
                    JOptionPane.showMessageDialog(null, "Account created successfully!");
                } catch (BadDataException bde) {
                    JOptionPane.showMessageDialog(null, bde.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                boolean validLogin = false;
                while (!validLogin) {
                    username = JOptionPane.showInputDialog("Enter username:");
                    password = JOptionPane.showInputDialog("Enter password:");

                    String loginResponse = loginSignUP.authorizeLogin(username, password);

                    if (loginResponse.equals("Wrong password! Please try again.")) {
                        JOptionPane.showMessageDialog(null, "Wrong password! Please try again.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    else if (loginResponse.equals("Username does not exist")) {
                        JOptionPane.showMessageDialog(null, "Username does not exist! Please try again.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    else {
                        String[] loginInfo = loginSignUP.authorizeLogin(username, password).split(",");

                        String authorizedUser = loginInfo[0];
                        String authorizedPass = loginInfo[1];
                        validLogin = true;
                        loggedInUsername = username;
                        // Additional logic for successful login actions
                    }
                }

            } else {
                // Login flow
                boolean validLogin = false;
                while (!validLogin) {
                    username = JOptionPane.showInputDialog("Enter username:");
                    password = JOptionPane.showInputDialog("Enter password:");

                    String loginResponse = loginSignUP.authorizeLogin(username, password);

                    if (loginResponse.equals("Wrong password! Please try again.")) {
                        JOptionPane.showMessageDialog(null, "Wrong password! Please try again.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    else if (loginResponse.equals("Username does not exist")) {
                        JOptionPane.showMessageDialog(null, "Username does not exist! Please try again.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    else {
                        String[] loginInfo = loginSignUP.authorizeLogin(username, password).split(",");

                        String authorizedUser = loginInfo[0];
                        String authorizedPass = loginInfo[1];
                        validLogin = true;
                        loggedInUsername = username;
                        // Additional logic for successful login actions
                    }
                }
            }

            // End login

            while (true) {
                String input = JOptionPane.showInputDialog(null, "Enter the command:");
                if (input == null || input.isEmpty()) {
                    break;
                }

                String[] inputArr = input.split(",");
                input = inputArr[0] + "," + loggedInUsername + "," + inputArr[1];

                pw.println(input);
                String response = bfr.readLine();

                JOptionPane.showMessageDialog(null, response);

                int choiceForAnotherMessage = JOptionPane.showConfirmDialog(null, "Would you like to send another command?", "Continue", JOptionPane.YES_NO_OPTION);
                if (choiceForAnotherMessage != JOptionPane.YES_OPTION) {
                    pw.println("exit");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
