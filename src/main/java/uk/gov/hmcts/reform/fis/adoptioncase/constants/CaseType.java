package uk.gov.hmcts.reform.fis.adoptioncase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CaseType {
    A_58("A58"), ADOPTION("ADOPTION"), PRL("PRL");

    private final String name;
}
