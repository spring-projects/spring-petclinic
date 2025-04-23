public class UserService {
    public String getUserName(int userId) {
        return "[Feature] User: " + userId;
    }

    public boolean isUserActive(int userId) {
        System.out.println("Checking user activity in Feature branch");
        return false;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        System.out.println("Feature branch updating user status");
        // Extra logic here
    }

    public void deleteUser(int userId) {
        System.out.println("Feature branch: deleting user " + userId);
    }

    public void notifyUser(int userId, String message) {
        System.out.println("[Feature] Notifying user: " + message);
        // Extra feature logic
    }
}
