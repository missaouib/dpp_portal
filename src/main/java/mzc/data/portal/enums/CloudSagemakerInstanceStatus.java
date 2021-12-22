package mzc.data.portal.enums;

public enum CloudSagemakerInstanceStatus {
    DELETING("삭제중"),
    FAILED("실패"),
    IN_SERVICE("사용중"),
    PENDING("처리중"),
    STOPPED("삭제"),
    STOPPING("삭제중"),
    UNKNOWN_TO_SDK_VERSION("에러"),
    UPDATING("수정중");

    private final String displayStatus;

    private CloudSagemakerInstanceStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
