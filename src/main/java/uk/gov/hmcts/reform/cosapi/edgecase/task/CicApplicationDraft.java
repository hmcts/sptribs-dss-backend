package uk.gov.hmcts.reform.cosapi.edgecase.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;

@Component
@Slf4j
public class CicApplicationDraft implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final Long caseId = caseDetails.getId();

        log.info("Executing handler for generating draft application for case id {} ", caseId);

        return caseDetails;
    }
}
