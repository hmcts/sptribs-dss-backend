package uk.gov.hmcts.reform.cosapi.edgecase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CaseType {
    A_100("A100"), A_200("A200"), ADOPTION("ADOPTION"), PRL("PRIVATELAW"), CRU("CRU");

    private final String name;
}
