package uk.gov.hmcts.reform.cosapi.edgecase.workbasket.adoption;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

@Component
public class AdoptionWorkBasketResultFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .workBasketResultFields()
            .caseReferenceField()
            //.field("applicantHomeAddress", "Applicant's Post Code", "PostCode")
            .field("applicantLastName", "Applicant's Last Name");
    }
}
