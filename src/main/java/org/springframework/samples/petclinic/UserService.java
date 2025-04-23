// File: UserService.java
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
}
