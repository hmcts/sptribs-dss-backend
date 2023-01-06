package uk.gov.hmcts.reform.cosapi.edgecase.event.cic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.util.AppsUtil;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CREATOR;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class UpdateCaseEvent implements CCDConfig<CaseData, State, UserRole>  {

    @Autowired
    AppsConfig appsConfig;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(AppsUtil.getExactAppsDetailsByCaseType(appsConfig, CommonConstants.ST_CIC_CASE_TYPE).getEventIds()
                       .getUpdateEvent())
            .forStates(State.DRAFT, State.SUBMITTED)
            .name("Edge case")
            .description("Edge case application update")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(CREATE_READ_UPDATE, CREATOR);
    }

}
