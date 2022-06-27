package uk.gov.hmcts.reform.cosapi.edgecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions;


@Component
public class PrivateLawEdgeCase implements CCDConfig<CaseData, State, UserRole> {

    @Autowired
    AppsConfig appsConfig;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));
        configBuilder.caseType(CommonConstants.PRL_CASE_TYPE, "New edge case", "Handling of edge cases");
        configBuilder.jurisdiction(CommonConstants.PRL_JURISDICTION, "Prl jurisdiction adoption", "edge-cases");
        configBuilder.grant(State.DRAFT, Permissions.CREATE_READ_UPDATE, UserRole.CITIZEN);
    }
}

