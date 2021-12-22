package mzc.data.portal.enums;

public enum MasterResourceStatus {
    ACTIVE("사용"),
    INACTIVE("중지");

    private final String displayStatus;

    private MasterResourceStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
