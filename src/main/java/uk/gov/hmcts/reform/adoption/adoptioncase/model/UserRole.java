package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    ADOPTION_GENERIC("caseworker-adoption", "CRU"),
    CASE_WORKER("caseworker-adoption-caseworker", "CRU"),
    COURT_ADMIN("caseworker-adoption-courtadmin", "CRU"),
    LEGAL_ADVISOR("caseworker-adoption-la", "CRU"),
    DISTRICT_JUDGE("caseworker-adoption-judge", "CRU"),
    SUPER_USER("caseworker-adoption-superuser", "CRU"),
    SOLICITOR("caseworker-adoption-solicitor", "CRU"),
    CITIZEN("citizen", "CRUD"),
    CREATOR("[CREATOR]", "CRU");

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
}
