package mzc.data.portal.enums;

public enum UserGrade {
    ROLE_ADMIN("관리자"),
    ROLE_USER("사용자"),
    ROLE_INIT("계정 초기화");

    private final String displayGrade;

    private UserGrade(String displayGrade) {
        this.displayGrade = displayGrade;
    }

    public String getDisplayGrade() {
        return displayGrade;
    }
}
