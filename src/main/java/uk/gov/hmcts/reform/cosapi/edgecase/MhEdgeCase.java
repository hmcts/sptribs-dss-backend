package uk.gov.hmcts.reform.cosapi.edgecase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions;

@Component
public class MhEdgeCase implements CCDConfig<CaseData, State, UserRole> {


    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL",
                                                                   "http://localhost:4550"));
        configBuilder.caseType(CommonConstants.ST_MH_CASE_TYPE, "New edge case", "Handling of edge cases");
        configBuilder.jurisdiction(CommonConstants.ST_MH_JURISDICTION,
                                   "ST jurisdiction MH", "edge-cases");
        configBuilder.grant(State.DRAFT, Permissions.CREATE_READ_UPDATE, UserRole.CITIZEN);
        configBuilder.event("");
    }
}
