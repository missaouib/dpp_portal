package mzc.data.portal.enums;

public enum DataSearchDeleteYN {
    Y("삭제"),
    N("유지");

    private final String displayDeleteYN;

    private DataSearchDeleteYN(String displayDeleteYN) {
        this.displayDeleteYN = displayDeleteYN;
    }

    public String getDisplayDeleteYN() {
        return displayDeleteYN;
    }
}
