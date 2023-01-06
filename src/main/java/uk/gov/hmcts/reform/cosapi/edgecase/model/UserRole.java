package uk.gov.hmcts.reform.cosapi.edgecase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {
    ST_CIC_GENERIC(UserRoleConstant.ST_CIC_GENERIC, UserRoleConstant.CASE_TYPE_PERMISSIONS_CRU),
    COURT_ADMIN(UserRoleConstant.COURT_ADMIN, UserRoleConstant.CASE_TYPE_PERMISSIONS_CRU),
    SOLICITOR(UserRoleConstant.SOLICITOR, UserRoleConstant.CASE_TYPE_PERMISSIONS_CRU),
    CITIZEN(UserRoleConstant.CITIZEN, UserRoleConstant.CASE_TYPE_PERMISSIONS_CRU),
    CREATOR(UserRoleConstant.CREATOR, UserRoleConstant.CASE_TYPE_PERMISSIONS_CRUD);

    @JsonValue
    private final String role;
    private final String caseTypePermissions;
}
