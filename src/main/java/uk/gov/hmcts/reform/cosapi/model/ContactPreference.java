package uk.gov.hmcts.reform.cosapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContactPreference {

    ACCOUNT_OWNER,
    NAMED_PERSON,
    BOTH_RECEIVE
}