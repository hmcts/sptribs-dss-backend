package uk.gov.hmcts.reform.fis.edgecase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.fis.edgecase.model.PrivateLawCaseData;
import uk.gov.hmcts.reform.fis.edgecase.model.State;
import uk.gov.hmcts.reform.fis.edgecase.model.UserRole;

import static uk.gov.hmcts.reform.fis.edgecase.model.State.DRAFT;
import static uk.gov.hmcts.reform.fis.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.fis.edgecase.model.access.Permissions.CREATE_READ_UPDATE;


@Component
public class PrivateLawEdgeCase implements CCDConfig<PrivateLawCaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<PrivateLawCaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));
        configBuilder.caseType("A60", "New PrivateLaw case", "Handling of child adoption case");
        configBuilder.jurisdiction("PRIVATELAW", "Family jurisdiction adoption", "Child adoption");
        configBuilder.grant(DRAFT, CREATE_READ_UPDATE, CITIZEN);
    }
}

