package uk.gov.hmcts.reform.cosapi.edgecase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@RequiredArgsConstructor
@Getter
public enum State {
    @CCD(
        name = "Draft",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicantFirstName} ${applicantLastName}\n"
    )
    DRAFT("Draft"),

    @CCD(
        name = "Submitted",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicantFirstName} ${applicantLastName}\n"
    )
    SUBMITTED("Submitted"),

    @CCD(
        name = "Application awaiting payment",
        label = "### Case number: ${[CASE_REFERENCE]}\n ### ${applicantFirstName} ${applicantLastName}\n"
    )
    AWAITING_PAYMENT("AwaitingPayment");

    private final String name;

}

