package mzc.data.portal.enums;

public enum CloudSandboxInstanceStatus {
    STARTING("시작 중"),
    RUNNING("실행 중"),
    STOPPING("중지 중"),
    STOPPED("중지"),
    TERMINATED("삭제"),
    PROGRESS("삭제 신청"),
    ERROR("에러");

    private final String displayStatus;

    private CloudSandboxInstanceStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
