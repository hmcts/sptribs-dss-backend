package uk.gov.hmcts.reform.cosapi.edgecase.event.adoption;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CREATOR;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.access.Permissions.READ;


@Component
public class AdoptionCitizenUpdateApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String CITIZEN_UPDATE = "citizen-update-application";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .event(CITIZEN_UPDATE)
            .forStates(State.DRAFT, State.SUBMITTED)
            .name("Edge case")
            .description("Edge case application update")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(CREATE_READ_UPDATE, CREATOR)
            .grant(READ, SUPER_USER);
    }
}
