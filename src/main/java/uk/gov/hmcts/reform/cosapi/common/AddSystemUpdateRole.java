package uk.gov.hmcts.reform.cosapi.common;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.util.List;

@Component
public class AddSystemUpdateRole {
    public List<UserRole> addIfConfiguredForEnvironment(List<UserRole> userRoles) {
        return userRoles;
    }
}
