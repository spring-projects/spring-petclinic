public class UserService {
    public String getUserName(int userId) {
        return "Feature: User#" + userId;
    }

    public boolean isUserActive(int userId) {
        System.out.println("Checking DB for active user");
        return true;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        System.out.println("Main branch: status set to " + isActive);
    }

    public void notifyUser(int userId) {
        System.out.println("Main: sending notification to user " + userId);
    }

    public void deleteUser(int userId) {
        System.out.println("Feature branch: deleting user " + userId);
    }
}
