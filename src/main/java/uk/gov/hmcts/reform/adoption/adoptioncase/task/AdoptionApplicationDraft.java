package uk.gov.hmcts.reform.adoption.adoptioncase.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;

@Component
@Slf4j
public class AdoptionApplicationDraft implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final Long caseId = caseDetails.getId();

        log.info("Executing handler for generating draft adoption application for case id {} ", caseId);

        return caseDetails;
    }
}
