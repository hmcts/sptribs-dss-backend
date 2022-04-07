package uk.gov.hmcts.reform.cosapi.edgecase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CaseType {
    A_58("A58"),A_60("A60"), ADOPTION("ADOPTION"), PRL("PRIVATELAW"), CRU("CRU");

    private final String name;
}
