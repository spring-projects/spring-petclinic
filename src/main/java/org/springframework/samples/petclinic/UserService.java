public class UserService {
    public String getUserName(int userId) {
        return "[Main] User#" + userId;
    }

    public boolean isUserActive(int userId) {
        System.out.println("Main branch: checking DB for user activity");
        return true;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        System.out.println("Main branch: logging status update");
        // update logic to DB
    }

    public void deleteUser(int userId) {
        logDeletion(userId);
        // deletion logic
    }

    public void notifyUser(int userId, String message) {
        System.out.println("[Main] Sending message to user: " + message);
    }

    private void logDeletion(int userId) {
        System.out.println("Logging deletion of user " + userId);
    }
}
