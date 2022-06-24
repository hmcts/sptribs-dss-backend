package uk.gov.hmcts.reform.cosapi.edgecase.model;

public final class UserRoleConstant {

    public static final String ADOPTION_GENERIC = "caseworker-privatelaw";
    public static final String CASE_WORKER = "caseworker-family-caseworker";
    public static final String COURT_ADMIN = "caseworker-privatelaw-courtadmin";
    public static final String LEGAL_ADVISOR = "caseworker-family-la";
    public static final String DISTRICT_JUDGE = "caseworker-family-judge";
    public static final String SUPER_USER = "caseworker-family-superuser";
    public static final String SOLICITOR = "caseworker-privatelaw-solicitor";
    public static final String CITIZEN = "citizen";
    public static final String CREATOR = "[CREATOR]";
    public static final String CASE_TYPE_PERMISSIONS_CRU = "CRU";
    public static final String CASE_TYPE_PERMISSIONS_CRUD = "CRUD";

    private UserRoleConstant() {
        //private constructor
    }
}
