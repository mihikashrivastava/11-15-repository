# Phase 1 ReadMe

## Description

Welcome to the first phase of our Direct Messaging App. In this phase, we focused on laying out a solid foundation by building out the user interface and the supporting database structure. We have written the following classes: `LoginSignUp`, `Message`, `MessageManager`, and `User`. These classes implement the interfaces `LoginSignUpService`, `MessageService`, and `UserService`. To ensure smooth program functionality, we also created the exception classes `BadDataException` and `UserNotFoundException`.

---

### LoginSignUp.java

Handles user authentication and registration processes, such as logging in and creating a new account, by checking a text file that stores account data and login information. This class implements the interface `LoginSignUpService`, which defines `authorizeLogin` and `createNewUser`.

#### Fields

| Name               | Return Type | Description                                              |
|--------------------|-------------|----------------------------------------------------------|
| `username`         | String      | Stores the username entered by the user                  |
| `password`         | String      | Stores the password entered by the user                  |
| `newAccount`       | Boolean     | Tracks if a new account is being created                 |
| `authorization`    | Boolean     | Indicates if the login attempt is successful             |
| `passwordCorrect`  | Boolean     | Indicates if the password matches the correct username   |
| `NO_USERNAME`      | Constant    | Holds the error message when the username doesn’t exist  |
| `PASSWORD_INCORRECT` | Constant | Holds the error message when the password is incorrect   |

#### Methods

| Name             | Modifier | Return Type | Parameters                                            | Description                                                                                   |
|------------------|----------|-------------|-------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| `authorizeLogin` | public   | String      | `String username`, `String password`                  | Takes a username and password, checks if valid by reading from `accounts.txt`. Returns errors if invalid. |
| `createNewUser`  | public   | User        | `String username`, `String password`, `String email`, `String firstName`, `String lastName`, `String profileImage` | Checks if username exists, validates password format, then creates a User and adds to `accounts.txt`. Throws `BadDataException` if invalid data is entered. |

---

### Message.java

Represents a message sent between two `User` objects in a messaging system. Supports both text and image messaging, and includes metadata such as sender, receiver, and timestamp. This class implements the `MessageService` interface.

#### Fields

| Name          | Return Type    | Description                                                    |
|---------------|----------------|----------------------------------------------------------------|
| `messageId`   | int            | A unique identifier for the message                            |
| `message`     | String         | The text content of the message (text)                         |
| `isImage`     | Boolean        | Indicates if a message is an image or text (true = image)      |
| `sender`      | User           | The user who sends the message                                 |
| `receiver`    | User           | The user who receives the message                              |
| `timestamp`   | LocalDateTime  | The time the message was sent                                  |
| `image`       | byte[]         | The image data, stored as a byte array                         |

#### Constructor

| Name      | Parameters                                                                            | Description                                       |
|-----------|---------------------------------------------------------------------------------------|---------------------------------------------------|
| `Message` | `String message`, `boolean isImage`, `User sender`, `User receiver`, `LocalDateTime timestamp`, `byte[] image` | Initializes a `Message` with the provided details.|

#### Methods

| Name             | Modifier | Return Type    | Parameters             | Description                           |
|------------------|----------|----------------|------------------------|---------------------------------------|
| `getMessageId`   | public   | int            | N/A                    | Accessor for `messageId`             |
| `setMessageId`   | public   | void           | `int messageId`        | Mutator for `messageId`              |
| `getMessage`     | public   | String         | N/A                    | Accessor for `message`               |
| `setMessage`     | public   | void           | `String message`       | Mutator for `message`                |
| `getSender`      | public   | User           | N/A                    | Accessor for `sender`                |
| `setSender`      | public   | void           | `User sender`          | Mutator for `sender`                 |
| `getReceiver`    | public   | User           | N/A                    | Accessor for `receiver`              |
| `setReceiver`    | public   | void           | `User receiver`        | Mutator for `receiver`               |
| `getTimestamp`   | public   | LocalDateTime  | N/A                    | Accessor for `timestamp`             |
| `setTimestamp`   | public   | void           | `LocalDateTime timestamp` | Mutator for `timestamp`          |
| `getImage`       | public   | byte[]         | N/A                    | Accessor for `image`                 |
| `setImage`       | public   | void           | `byte[] image`         | Mutator for `image`                  |
| `isImage`        | public   | boolean        | N/A                    | Accessor for `isImage`               |
| `setImage`       | public   | void           | `boolean isImage`      | Mutator for `isImage` flag           |

---

### MessageManager.java

Manages messaging functions, user blocking/unblocking, friend management, and friend requests within the messaging system. Uses serialization for file storage.

#### Methods

| Name                  | Modifier | Return Type | Parameters                               | Description                                                                            |
|-----------------------|----------|-------------|------------------------------------------|----------------------------------------------------------------------------------------|
| `sendMessage`         | public   | void        | `User to`, `User from`, `Message message` | Sends a message by serializing the message list to a file for the sender and receiver. |
| `createMessageObject` | public   | Message     | `String message`, `boolean isImage`, `User sender`, `User receiver`, `LocalDateTime timestamp`, `byte[] image` | Creates and returns a `Message` object. |
| `block`               | public   | boolean     | `User userFrom`, `User userTo`           | Blocks `userTo` for `userFrom` if not already blocked. Throws `UserNotFoundException`.  |
| `unblock`             | public   | boolean     | `User userFrom`, `User userTo`           | Unblocks `userTo` for `userFrom`. Throws `UserNotFoundException`.                      |
| `removeFriend`        | public   | boolean     | `User userFrom`, `User userTo`           | Removes `userTo` from `userFrom`’s friend list if they are friends.                    |
| `search`              | public   | boolean     | `User user`                              | Searches for a user in the `users.bin` file.                                           |
| `sendFriendRequest`   | public   | boolean     | `User userFrom`, `User userTo`           | Sends a friend request from `userFrom` to `userTo` if criteria are met.                |
| `acceptFriendRequest` | public   | boolean     | `User userFrom`, `User userTo`           | Accepts a friend request. Throws `UserNotFoundException` if `userTo` doesn’t exist.    |
| `rejectFriendRequest` | public   | boolean     | `User userFrom`, `User userTo`           | Rejects a friend request. Throws `UserNotFoundException`.                              |

---

### User.java

Models a user in the messaging system and implements the `UserService` interface. Provides fields and methods for managing user data, friends, blocking, and friend requests.

#### Fields

| Name                     | Return Type           | Description                              |
|--------------------------|-----------------------|------------------------------------------|
| `username`               | String                | The unique identifier for the user       |
| `password`               | String                | The user’s password for authentication   |
| `email`                  | String                | The user’s email address                 |
| `firstName`              | String                | The user’s first name                    |
| `lastName`               | String                | The user’s last name                     |
| `profileImage`           | String                | The path to the user’s profile image     |
| `lastLoginAt`            | LocalDateTime         | The timestamp of the user’s last login   |
| `friends`                | ArrayList<User>       | A list of the user’s friends             |
| `blocked`                | ArrayList<User>       | A list of users blocked by this user     |
| `requestedFriend`        | ArrayList<User>       | Users to whom this user has sent requests |
| `requestedFriends`       | ArrayList<User>       | Users who sent friend requests to this user |
| `pendingFriendRequests`  | ArrayList<User>       | Friend requests awaiting user’s approval |
| `privateAccount`         | boolean               | Indicates if the account is private      |

#### Constructors

| Name    | Parameters                                    | Description                                   |
|---------|-----------------------------------------------|-----------------------------------------------|
| `User`  | `String userName`, `String password`, `String email`, `String firstName`, `String lastName`, `String profileImage` | Initializes a new `User` with details and empty lists for friends, blocked users, and friend requests. |

| Method                      | Modifier | Return Type               | Parameters                       | Description                                                                                             |
|-----------------------------|----------|---------------------------|----------------------------------|---------------------------------------------------------------------------------------------------------|
| getUsername                 | public   | String                    | N/A                              | Gets/sets the user’s username.                                                                          |
| setUsername                 | void     |                           | String username                  | Sets the user’s username.                                                                                |
| getPassword                 | public   | String                    | N/A                              | Gets/sets the user’s password.                                                                          |
| setPassword                 | void     |                           | String password                  | Sets the user’s password.                                                                                |
| getEmail                    | public   | String                    | N/A                              | Gets/sets the user’s email address.                                                                     |
| setEmail                    | void     |                           | String email                     | Sets the user’s email address.                                                                           |
| getProfileImage             | public   | String                    | N/A                              | Gets/sets the user’s profile image.                                                                     |
| setProfileImage             | void     |                           | String profileImage              | Sets the user’s profile image.                                                                           |
| getLastLoginAt             | public   | LocalDateTime             | N/A                              | Gets/sets the timestamp of the user’s last login.                                                      |
| setLastLoginAt             | void     |                           | LocalDateTime lastLoginAt       | Sets the timestamp of the user’s last login.                                                            |
| getFirstName               | public   | String                    | N/A                              | Gets/sets the user’s first name.                                                                        |
| setFirstName               | void     |                           | String firstName                | Sets the user’s first name.                                                                              |
| getLastName                | public   | String                    | N/A                              | Gets/sets the user’s last name.                                                                         |
| setLastName                | void     |                           | String lastName                 | Sets the user’s last name.                                                                               |
| getFriends                  | public   | ArrayList<User>           | N/A                              | Returns the list of the user’s friends.                                                                 |
| addFriend                   | public   | void                      | User friend                     | Adds a friend to the user’s friends list.                                                                |
| getFriend                   | public   | int                       | User friend                     | Returns the index of the friend in the friends list.                                                   |
| getPendingFriendRequests     | public   | ArrayList<User>           | N/A                              | Returns the list of pending friend requests.                                                             |
| getRequestedFriends         | public   | ArrayList<User>           | N/A                              | Returns the list of users who have sent friend requests to this user.                                   |
| removeFriend                | public   | boolean                   | User friend                     | Removes a friend from the user’s friends list if they exist.                                           |
| addBlocked                  | public   | boolean                   | User blockedUser                | Adds a user to the blocked list if they already aren’t.                                                |
| getBlocked                  | public   | ArrayList<User>           | N/A                              | Returns the list of blocked users.                                                                       |
| removeBlocked               | public   | boolean                   | User blockedUser                | Removes a user from the blocked list if they are currently blocked.                                     |
| addUserToFile              | public   | void                      | User user                       | Adds the user to “users.bin” for persistence and reads the existing users from the file, appends the new user, and writes the updated list back to the file. |
| changeVisibility            | public   | boolean                   | N/A                              | Toggles the visibility of the user’s account between private (only friends can message them) and public (anyone can message them). |


---



### Exceptions

Exceptions for handling invalid or bad data in the application. They are used to manage specific errors for data validation and ensure program stability.

#### List of Exceptions

| Name                  | Modifier | Parameters         | Description                                                             |
|-----------------------|----------|--------------------|-------------------------------------------------------------------------|
| `BadDataException`    | public   | `String message`   | Thrown when user input is invalid or doesn’t follow expected rules      |
| `UserNotFoundException` | public | `String message`   | Thrown when trying to interact with a non-existent user                 |

---

### Data Storage Overview

In our application, account credentials such as usernames and passwords are stored in a text file as comma-separated strings for validation purposes. User and Message data are serialized and stored in binary files. Each `User` object (with details like username, password, email, friends, blocked users, and friend requests) is stored in `users.bin`. Messages between users are stored in files containing serialized `Message` objects, each representing either a text or image message with metadata.

---
