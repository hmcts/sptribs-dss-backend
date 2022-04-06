package uk.gov.hmcts.reform.fis.edgecase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

import static uk.gov.hmcts.reform.fis.edgecase.constants.CaseType.CRU;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {

    ADOPTION_GENERIC("caseworker-adoption", CRU.toString()),
    CASE_WORKER("caseworker-adoption-caseworker", CRU.toString()),

    COURT_ADMIN("caseworker-adoption-courtadmin", CRU.toString()),
    LEGAL_ADVISOR("caseworker-adoption-la", CRU.toString()),
    DISTRICT_JUDGE("caseworker-adoption-judge", CRU.toString()),
    SUPER_USER("caseworker-adoption-superuser", CRU.toString()),
    SOLICITOR("caseworker-adoption-solicitor", CRU.toString()),
    CITIZEN("citizen", "CRUD"),
    CREATOR("[CREATOR]", CRU.toString());

    @JsonValue
    private final String role;
    private final String caseTypePermissions;

}
