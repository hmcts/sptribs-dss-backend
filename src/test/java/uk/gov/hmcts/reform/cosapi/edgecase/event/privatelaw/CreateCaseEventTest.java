package uk.gov.hmcts.reform.cosapi.edgecase.event.privatelaw;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.Permission;
import uk.gov.hmcts.reform.cosapi.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.util.AppsUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.ccd.sdk.api.Permission.C;
import static uk.gov.hmcts.ccd.sdk.api.Permission.R;
import static uk.gov.hmcts.ccd.sdk.api.Permission.U;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.createCaseDataConfigBuilder;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.getEventsFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CreateCaseEventTest {

    @Mock
    private AddSystemUpdateRole addSystemUpdateRole;

    @InjectMocks
    private CreateCaseEvent createCaseEvent;

    @Autowired
    AppsConfig appsConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void shouldAddConfigurationToConfigBuilderAndSetPermissionOnlyForCitizenRole() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        when(addSystemUpdateRole.addIfConfiguredForEnvironment(anyList()))
            .thenReturn(List.of(CITIZEN));

        createCaseEvent.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(AppsUtil.getExactAppsDetailsByCaseType(appsConfig, CommonConstants.PRL_CASE_TYPE).getEventIds()
                          .getCreateEvent());

        SetMultimap<UserRole, Permission> expectedRolesAndPermissions =
            ImmutableSetMultimap.<UserRole, Permission>builder()
            .put(CITIZEN, C)
            .put(CITIZEN, R)
            .put(CITIZEN, U)
            .build();

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getGrants)
            .containsExactly(expectedRolesAndPermissions);
    }

//    @Test
//    void shouldSetPermissionForCitizenAndCaseWorkerRoleWhenEnvironmentIsAat() {
//        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
//
//        when(addSystemUpdateRole.addIfConfiguredForEnvironment(anyList()))
//            .thenReturn(List.of(CITIZEN, CASE_WORKER));
//
//        createCaseEvent.configure(configBuilder);
//
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getId)
//            .contains(CREATE_CASE_EVENT_ID);
//
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getDescription)
//            .contains("Apply for edge case");
//
//        SetMultimap<UserRole, Permission> expectedRolesAndPermissions =
//            ImmutableSetMultimap.<UserRole, Permission>builder()
//            .put(CITIZEN, C)
//            .put(CITIZEN, R)
//            .put(CITIZEN, U)
//            .put(CASE_WORKER, C)
//            .put(CASE_WORKER, R)
//            .put(CASE_WORKER, U)
//            .build();
//
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getGrants)
//            .containsExactlyInAnyOrder(expectedRolesAndPermissions);
//    }
}
