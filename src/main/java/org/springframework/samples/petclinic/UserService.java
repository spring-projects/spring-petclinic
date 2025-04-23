public class UserService {
    public String getUserName(int userId) {
        return "Feature: User#" + userId;
    }

    public boolean isUserActive(int userId) {
        return false;
    }

    public void updateUserStatus(int userId, boolean isActive) {
        System.out.println("Feature branch: updating status to " + isActive);
    }

    public void notifyUser(int userId) {
        System.out.println("Feature branch: notifying user " + userId);
    }

    public void deleteUser(int userId) {
        System.out.println("Feature branch: deleting user " + userId);
    }
}
