package mzc.data.portal.enums;

public enum SandboxType {
    EC2("EC2"),
    SAGEMAKER("SageMaker");

    private final String displayStatus;

    private SandboxType(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
