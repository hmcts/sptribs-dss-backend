package uk.gov.hmcts.reform.cosapi.edgecase.event.adoption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.cosapi.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.util.ArrayList;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.State.DRAFT;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class AdoptionCitizenCreateApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String CITIZEN_CREATE = "citizen-create-application";

    @Autowired
    private AddSystemUpdateRole addSystemUpdateRole;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var defaultRoles = new ArrayList<UserRole>();
        defaultRoles.add(CITIZEN);

        var updatedRoles = addSystemUpdateRole.addIfConfiguredForEnvironment(defaultRoles);

        configBuilder
            .event(CITIZEN_CREATE)
            .initialState(DRAFT)
            .name("Create edge case draft case")
            .description("Apply for edge case")
            .aboutToSubmitCallback(this::aboutToSubmit)
            .grant(CREATE_READ_UPDATE, updatedRoles.toArray(UserRole[]::new))
            .retries(120, 120);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                 final CaseDetails<CaseData, State> beforeDetails) {
        log.info("Citizen create edge case application about to submit callback invoked");

        CaseData data = details.getData();
        data.setHyphenatedCaseRef(data.formatCaseRef(details.getId()));

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}

