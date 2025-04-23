public class UserService {
    public String getUserName(int userId) {
        return "User" + userId;
    }

    public boolean isUserActive(int userId) {
        return true;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        // update logic here
    }

    public void deleteUser(int userId) {
        // deletion logic
    }

    public void notifyUser(int userId, String message) {
        System.out.println("Sending message: " + message);
    }
}
