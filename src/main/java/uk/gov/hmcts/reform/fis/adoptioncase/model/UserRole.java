package uk.gov.hmcts.reform.fis.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    ADOPTION_GENERIC("caseworker-adoption", Constants.CRU),
    CASE_WORKER("caseworker-adoption-caseworker", Constants.CRU),
    COURT_ADMIN("caseworker-adoption-courtadmin", Constants.CRU),
    LEGAL_ADVISOR("caseworker-adoption-la", Constants.CRU),
    DISTRICT_JUDGE("caseworker-adoption-judge", Constants.CRU),
    SUPER_USER("caseworker-adoption-superuser", Constants.CRU),
    SOLICITOR("caseworker-adoption-solicitor", Constants.CRU),
    CITIZEN("citizen", "CRUD"),
    CREATOR("[CREATOR]", Constants.CRU);

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

    @Override
    public String getCaseTypePermissions() {
        return "CRUD";
    }

    @Override
    public String getRole() {
        return "citizen";
    }

    private static class Constants {
        public static final String CRU = "CRU";
    }
}
