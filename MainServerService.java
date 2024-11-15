public interface MainServerService {
  public static User searchUserFromUsername(String username) throws UserNotFoundException;
  public static String processCommand(String inputFromClient, MessageManager messageManager);
}
