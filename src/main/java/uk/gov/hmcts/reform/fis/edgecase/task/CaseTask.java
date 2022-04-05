package uk.gov.hmcts.reform.fis.edgecase.task;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.fis.edgecase.model.CaseData;
import uk.gov.hmcts.reform.fis.edgecase.model.State;

import java.util.function.Function;

public interface CaseTask extends Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>> {
}
