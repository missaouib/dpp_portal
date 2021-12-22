package mzc.data.portal.enums;

public enum SandboxSpecStatus {
    ACTIVE("사용"),
    INACTIVE("중지");

    private final String displayStatus;

    private SandboxSpecStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
