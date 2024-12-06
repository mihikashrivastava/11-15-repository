# Direct Messaging App - Phase 3

## Description

Welcome to the third phase of our Direct Messaging App. In this phase, we created GUIs for our app to create a better user experience and interaction and make the app more visually appealing. To create this, we used Java’s Swing class.

## MainClient.java

This class implements a basic client-side application for our Direct Messaging App. The application allows users to log in or register an account through a simple login and registration GUI. The main components include:

- **Login**
- **Register**
- **Socket Connection**
- **GUI Layout**

### Features

- The user can log into their account using their username and password.
- The user enters all five criteria for creating an account, and then can login using their created username and password.
- The client connects to a server running on "localhost" at port 8080. It utilizes `BufferedReader` and `PrintWriter` for reading from and writing to the server.
- The `MainClient` class handles the creation of the login and registration forms, including the layout and event handling. The login form uses `JTextField` for the username, `JPasswordField` for the password, and `JButton` components for submitting the form or navigating to the registration screen. The registration screen also uses similar Swing components to capture user details such as username, password, email, first name, and last name.

## MainGUI.java

The `MainGUI` class creates the main GUI for our social media app, which allows users to interact with their chats, send and delete messages, and manage conversations. The GUI provides a dynamic and responsive interface for chat interactions.

### Methods

| Name                   | Modifier | Return Type | Parameters                                      | Description                                                                                       |
|------------------------|----------|-------------|-------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `createGUI`            | public   | void        | String loggedInUsername, PrintWriter pw, BufferedReader bfr | Creates the user interface, initializes the three panels, loads chat data, and sets up the necessary components for chats, message sending, and user navigation. |
| `loadAndDisplayMessages` | private  | void        | String chatFileName, JPanel chatHistory, PrintWriter pw | Loads messages from the specified chat file and displays them in the right panel. Each message is displayed as a button and includes an option to delete it. |
| `monitorFileForChanges` | private  | void        | String chatFileName, JPanel chatHistory, JPanel rightPanel, PrintWriter pw | A thread that constantly monitors the chat file for new messages or deleted messages. When the file is updated, it refreshes the chat history in the GUI. |

```

Feel free to let me know if you need any further adjustments!
