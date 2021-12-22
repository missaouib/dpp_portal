package mzc.data.portal.enums;

public enum SandboxModelStatus {
    ACTIVE("사용"),
    INACTIVE("중지");

    private final String displayStatus;

    private SandboxModelStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
