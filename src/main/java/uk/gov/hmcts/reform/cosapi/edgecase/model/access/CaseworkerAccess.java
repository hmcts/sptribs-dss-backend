package uk.gov.hmcts.reform.cosapi.edgecase.model.access;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import uk.gov.hmcts.ccd.sdk.api.HasAccessControl;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.Permission;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.ST_CIC_GENERIC;

public class CaseworkerAccess implements HasAccessControl {

    @Override
    public SetMultimap<HasRole, Permission> getGrants() {
        SetMultimap<HasRole, Permission> grants = HashMultimap.create();
        grants.putAll(ST_CIC_GENERIC, Permissions.CREATE_READ_UPDATE);
        grants.putAll(CITIZEN, Permissions.CREATE_READ_UPDATE);

        return grants;
    }
}
