package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {

    CITIZEN("citizen", "CRUD");

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
