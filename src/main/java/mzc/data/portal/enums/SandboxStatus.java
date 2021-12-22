package mzc.data.portal.enums;

public enum SandboxStatus {
    WAITING("생성 요청"),
    REJECTED("생성 거절"),
    ACTIVE("승인"),
    PROGRESS("종료 신청"),
    TERMINATED("종료");

    private final String displayStatus;

    private SandboxStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
