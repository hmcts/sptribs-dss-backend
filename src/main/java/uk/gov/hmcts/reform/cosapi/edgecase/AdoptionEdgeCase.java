package uk.gov.hmcts.reform.cosapi.edgecase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions;


@Component
public class AdoptionEdgeCase implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));
        configBuilder.caseType("A58", "New adoption case", "Handling of child adoption case");
        configBuilder.jurisdiction("ADOPTION", "Family jurisdiction adoption", "Child adoption");
        configBuilder.grant(State.DRAFT, Permissions.CREATE_READ_UPDATE, UserRole.CITIZEN);
    }
}

