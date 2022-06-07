package uk.gov.hmcts.reform.cosapi.edgecase.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventEnum {
    SUBMIT("submit"),
    UPDATE("update");

    private final String eventType;
}
