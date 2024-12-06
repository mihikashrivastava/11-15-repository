
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

| Feature                | Description                                                                                       |
|------------------------|---------------------------------------------------------------------------------------------------|
| **Login**              | The user can log into their account using their username and password.                            |
| **Register**           | The user enters all five criteria for creating an account, and then can login using their created username and password. |
| **Socket Connection**  | The client connects to a server running on "localhost" at port 8080. It utilizes `BufferedReader` and `PrintWriter` for reading from and writing to the server. |
| **GUI Layout**         | The `MainClient` class handles the creation of the login and registration forms, including the layout and event handling. The login form uses `JTextField` for the username, `JPasswordField` for the password, and `JButton` components for submitting the form or navigating to the registration screen. The registration screen also uses similar Swing components to capture user details such as username, password, email, first name, and last name. |

## MainGUI.java

The `MainGUI` class creates the main GUI for our social media app, which allows users to interact with their chats, send and delete messages, and manage conversations. The GUI provides a dynamic and responsive interface for chat interactions.

### Methods

| Name                   | Modifier | Return Type | Parameters                                      | Description                                                                                       |
|------------------------|----------|-------------|-------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `createGUI`            | public   | void        | String loggedInUsername, PrintWriter pw, BufferedReader bfr | Creates the user interface, initializes the three panels, loads chat data, and sets up the necessary components for chats, message sending, and user navigation. |
| `loadAndDisplayMessages` | private  | void        | String chatFileName, JPanel chatHistory, PrintWriter pw | Loads messages from the specified chat file and displays them in the right panel. Each message is displayed as a button and includes an option to delete it. |
| `monitorFileForChanges` | private  | void        | String chatFileName, JPanel chatHistory, JPanel rightPanel, PrintWriter pw | A thread that constantly monitors the chat file for new messages or deleted messages. When the file is updated, it refreshes the chat history in the GUI. |

## SearchUserGUI.java

The `SearchUserGUI` class is responsible for managing the user interface where the logged-in user can search for other users, send friend requests, block/unblock users, and start conversations. This GUI is divided into three parts:

### Panels

| Panel         | Description                                                                                       |
|---------------|---------------------------------------------------------------------------------------------------|
| **Left Panel**  | Contains the navigation buttons "Chat", "Search", and "Profile".                                 |
| **Center Panel**| Allows the user to search for other users by username using a text field, and displays a list of search results in a scrollable list. |
| **Right Panel** | Shows action buttons for interacting with the selected user, such as sending a friend request, blocking or unblocking the user, and starting a conversation, if the conversation has not already been started. |

### Methods

| Name                   | Modifier | Return Type | Parameters                                      | Description                                                                                       |
|------------------------|----------|-------------|-------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `createSearchUserGUI`  | public   | void        | String loggedInUsername, PrintWriter pw, BufferedReader bfr | This method sets up the user interface, including creating the left, center, and right panels, along with adding buttons like "Send Friend Request", "Block User", and "Start Conversation". It handles user input by using a search field. |
| `searchUsers`          | private  | void        | none                                            | This method listens for input in the search field and updates the user list based on the search query entered by the user. It filters the list of users by matching the starting characters of their usernames with the query. |
| `showUserActions`      | private  | void        | none                                            | This method determines whether any user is selected in the `JList`. If a user is selected, it makes the action buttons (friend request, block/unblock, and send message) visible; otherwise, these buttons remain hidden. |

## How to Run

First, run `MainServer`, and then run `MainClient`. This will prompt you to the login page, where you can either enter an existing account’s username and password or create a new account.

## Testing Requirements

- **User Profile**: When you click the Profile button, you can see the details you registered your account with, like first name, last name, username, and email.
- **New User Account Creation**: When first running the application, click “register”. This will prompt you to enter the five required fields to create a new account.
- **Password Protected Login**: A user can only log into their account if the correct password is entered into the login GUI.
- **User Search**: Once logged in, click the “Search” button on the left side, and enter the username of the user you want to search for.
- **User Viewer**: When you search for a user and click on them, you can see their first name, last name, username, and email.
- **Add Friend**: After searching a user, click on that user. You then have the option to add friend.
- **Block User**: After searching a user, click on that user. You then have the option to block user.
- **Unblock User**: After searching a user, click on that user. You then have the option to unblock a user.
- **Send Message**: Click on the conversation with the user you want to send a message to, and enter your desired message in the text box.
- **Delete Message**: Click on the desired message you want to delete, and you will be prompted to either confirm that you want to delete that message or cancel.
- **Privacy Setting**: Accounts default to public. To change this, click on the profile button. Then, click on the “Private” button. This will now make it so that moving forward, only friends can message you. If you messaged a user who is not your friend before you changed your profile to private, you can continue to message them. However, no user who is not your friend can message you.

```

