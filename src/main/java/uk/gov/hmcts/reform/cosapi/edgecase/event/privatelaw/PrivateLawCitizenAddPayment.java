package uk.gov.hmcts.reform.cosapi.edgecase.event.privatelaw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.cosapi.edgecase.model.PrivateLawCaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.State.AWAITING_PAYMENT;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.READ;

@Component
@Slf4j
public class PrivateLawCitizenAddPayment implements CCDConfig<PrivateLawCaseData, State, UserRole> {

    public static final String CITIZEN_ADD_PAYMENT = "citizen-add-payment";

    @Override
    public void configure(final ConfigBuilder<PrivateLawCaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(CITIZEN_ADD_PAYMENT)
            .forState(AWAITING_PAYMENT)
            .name("Payment made")
            .description("Payment made")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(READ, SUPER_USER, CASE_WORKER)
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<PrivateLawCaseData, State> aboutToSubmit(
                                final CaseDetails<PrivateLawCaseData, State> details,
                                final CaseDetails<PrivateLawCaseData, State> beforeDetails) {
        //TODO logic needs to be updated separately as per edge-case application requirement
        return AboutToStartOrSubmitResponse.<PrivateLawCaseData, State>builder()
            .data(details.getData())
            .state(details.getState())
            .build();
    }
}

