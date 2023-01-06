package uk.gov.hmcts.reform.cosapi.edgecase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CaseType {
    CIC("CriminalInjuriesCompensation");

    private final String name;
}
