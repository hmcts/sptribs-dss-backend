package uk.gov.hmcts.reform.cosapi.edgecase.search.cic;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.SearchField;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.util.List;

import static java.util.List.of;
import static uk.gov.hmcts.reform.cosapi.edgecase.search.CaseFieldsConstants.FULL_NAME;
import static uk.gov.hmcts.reform.cosapi.edgecase.search.CaseFieldsConstants.SUBJECT_FULL_NAME;

@Component
public class CicSearchInputFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        final List<SearchField> searchFieldList = of(
            SearchField.builder().label(FULL_NAME).id(
                SUBJECT_FULL_NAME).build()
        );

        configBuilder.searchInputFields().caseReferenceField();
        configBuilder.searchInputFields().fields(searchFieldList);
    }
}
