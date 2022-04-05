package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentType implements HasLabel {
    @JsonProperty("birthOrAdoptionCertificate")
    BIRTH_OR_ADOPTION_CERTIFICATE("birthOrAdoptionCertificate"),

    @JsonProperty("deathCertificate")
    DEATH_CERTIFICATE("deathCertificate"),

    @JsonProperty("applicationSummaryEn")
    APPLICATION_SUMMARY_EN("applicationSummaryEn"),

    @JsonProperty("applicationSummaryCy")
    APPLICATION_SUMMARY_CY("applicationSummaryCy"),

    @JsonProperty("application")
    @JsonAlias("adoptionApplication")
    APPLICATION("Application"),

    @JsonProperty("email")
    EMAIL("Email");

    private final String label;
}
