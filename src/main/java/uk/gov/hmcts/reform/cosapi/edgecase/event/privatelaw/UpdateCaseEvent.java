package uk.gov.hmcts.reform.cosapi.edgecase.event.privatelaw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.util.ArrayList;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.State.DRAFT;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.*;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.READ;

@Component
@Slf4j
public class UpdateCaseEvent implements CCDConfig<CaseData, State, UserRole>  {

    @Autowired
    private AddSystemUpdateRole addSystemUpdateRole;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(CommonConstants.UPDATE_CASE_EVENT_ID)
            .forStates(State.DRAFT, State.SUBMITTED)
            .name("Edge case")
            .description("Edge case application update")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(CREATE_READ_UPDATE, CREATOR)
            .grant(READ, SUPER_USER);
    }

}
