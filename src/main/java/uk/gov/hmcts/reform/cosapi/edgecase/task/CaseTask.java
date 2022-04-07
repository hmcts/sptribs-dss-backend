package uk.gov.hmcts.reform.cosapi.edgecase.task;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;

import java.util.function.Function;

public interface CaseTask extends Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>> {
}
