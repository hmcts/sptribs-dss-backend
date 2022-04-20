package uk.gov.hmcts.reform.cosapi.edgecase.workbasket.privatelaw;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.edgecase.model.PrivateLawCaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

@Component
public class PrivatelawWorkBasketResultFields implements CCDConfig<PrivateLawCaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<PrivateLawCaseData, State, UserRole> configBuilder) {

        configBuilder
            .workBasketResultFields()
            .caseReferenceField()
            //.field("applicantHomeAddress", "Applicant's Post Code", "PostCode")
            .field("applicantLastName", "Applicant's Last Name");
    }
}
