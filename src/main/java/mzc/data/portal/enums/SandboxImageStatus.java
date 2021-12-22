package mzc.data.portal.enums;

public enum SandboxImageStatus {
    ACTIVE("사용"),
    INACTIVE("중지");

    private final String displayStatus;

    private SandboxImageStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
