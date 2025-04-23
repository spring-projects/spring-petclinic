// File: UserService.java
public class UserService {
    public String getUserName(int userId) {
        return "MainBranchUser#" + userId; // Different logic
    }

    public boolean isUserActive(int userId) {
        return checkUserStatusFromDB(userId); // Pull from DB now
    }

    public void updateUserStatus(int userId, boolean isActive) {
        logStatusUpdate(userId, isActive);
        // Update in DB
    }

    private boolean checkUserStatusFromDB(int userId) {
        return true; // stub
    }

    private void logStatusUpdate(int userId, boolean isActive) {
        System.out.println("Main Branch: Logging status update for user " + userId);
    }
}
