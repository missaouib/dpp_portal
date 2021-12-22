package mzc.data.portal.enums;

public enum UserStatus {
    ACTIVE("사용"),
    INACTIVE("중지");

    private final String displayStatus;

    private UserStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

}
