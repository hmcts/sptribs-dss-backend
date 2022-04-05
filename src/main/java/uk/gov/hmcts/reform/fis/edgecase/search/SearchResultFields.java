package uk.gov.hmcts.reform.fis.edgecase.search;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.fis.edgecase.model.CaseData;
import uk.gov.hmcts.reform.fis.edgecase.model.State;
import uk.gov.hmcts.reform.fis.edgecase.model.UserRole;

@Component
public class SearchResultFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .searchResultFields()
            .caseReferenceField();
    }
}
