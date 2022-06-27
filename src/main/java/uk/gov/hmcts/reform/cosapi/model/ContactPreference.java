package uk.gov.hmcts.reform.cosapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ContactPreference implements HasLabel {

    @JsonProperty("ao")
    ACCOUNT_OWNER("ao"),

    @JsonProperty("np")
    NAMED_PERSON("np"),

    @JsonProperty("bre")
    BOTH_RECEIVE("bre");

    private final String label;
}