// File: UserService.java
public class UserService {
    public String getUserName(int userId) {
        return "FeatureBranch_User_" + userId; // Changed logic
    }

    public boolean isUserActive(int userId) {
        return false; // Changed logic
    }

    public void updateUserStatus(int userId, boolean isActive) {
        System.out.println("Feature Branch: Updating user " + userId + " to " + (isActive ? "active" : "inactive"));
    }

    public void deactivateUser(int userId) {
        System.out.println("Feature Branch: Deactivating user " + userId);
    }
}
