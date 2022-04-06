package uk.gov.hmcts.reform.fis.edgecase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CaseType {
    A_58("A58"), ADOPTION("ADOPTION"), PRL("PRL"), CRU("CRU");

    private final String name;
}
