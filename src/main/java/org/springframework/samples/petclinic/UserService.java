public class UserService {
    public String getUserName(int userId) {
        return "User" + userId;
    }

    public boolean isUserActive(int userId) {
        return true;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        // Update user status in DB
    }

    public void notifyUser(int userId) {
        // Send notification
    }

    public void deleteUser(int userId) {
        // Delete user logic
    }
}
