package uk.gov.hmcts.reform.cosapi.edgecase.event.mh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.util.AppsUtil;

import java.util.ArrayList;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.State.DRAFT;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class MhCreateCaseEvent implements CCDConfig<CaseData, State, UserRole>  {

    @Autowired
    private AddSystemUpdateRole addSystemUpdateRole;

    @Autowired
    AppsConfig appsConfig;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var defaultRoles = new ArrayList<UserRole>();
        defaultRoles.add(CITIZEN);

        var updatedRoles = addSystemUpdateRole.addIfConfiguredForEnvironment(defaultRoles);

        configBuilder
            .event(AppsUtil.getExactAppsDetailsByCaseType(appsConfig, CommonConstants.ST_MH_CASE_TYPE).getEventIds()
                       .getCreateEvent())
            .initialState(DRAFT)
            .name("Create draft case (mh)")
            .description("Apply for edge case (mh)")
            .grant(CREATE_READ_UPDATE, updatedRoles.toArray(UserRole[]::new))
            .retries(120, 120);
    }

}
