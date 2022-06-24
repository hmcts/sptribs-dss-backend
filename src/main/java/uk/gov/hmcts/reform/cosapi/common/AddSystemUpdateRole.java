package uk.gov.hmcts.reform.cosapi.common;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.SUPER_USER;

@Component
public class AddSystemUpdateRole {
    public List<UserRole> addIfConfiguredForEnvironment(List<UserRole> userRoles) {
        return userRoles;
    }
}
