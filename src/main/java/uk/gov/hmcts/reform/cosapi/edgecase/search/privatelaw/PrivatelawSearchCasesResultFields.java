package uk.gov.hmcts.reform.cosapi.edgecase.search.privatelaw;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.edgecase.model.PrivateLawCaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

@Component
public class PrivatelawSearchCasesResultFields implements CCDConfig<PrivateLawCaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<PrivateLawCaseData, State, UserRole> configBuilder) {

        configBuilder
            .searchCasesFields()
            .field("[CASE_REFERENCE]", "Case Number", null, null, "1:ASC")
            .createdDateField()
            .field("applicantFirstName", "Applicant First Name")
            .lastModifiedDate()
            .stateField();
    }
}
