package uk.gov.hmcts.reform.cosapi.edgecase.event.cic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.cosapi.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;
import uk.gov.hmcts.reform.cosapi.util.AppsUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.createCaseDataConfigBuilder;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.getEventsFrom;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_CIC_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_CIC;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.LOCAL_DATE_TIME;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_UPDATE_CASE_EMAIL_ADDRESS;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application.yaml")
@ActiveProfiles("test")
class CicSubmitCaseEventTest {
    final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

    @InjectMocks
    private CicSubmitCaseEvent cicSubmitCaseEvent;

    @Mock
    private AddSystemUpdateRole addSystemUpdateRole;

    @Mock
    private AppsConfig appsConfig;

    @Mock
    private AppsConfig.AppsDetails fgmAppDetail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        fgmAppDetail = new AppsConfig.AppsDetails();
        fgmAppDetail.setCaseType(CommonConstants.ST_CIC_CASE_TYPE);
        fgmAppDetail.setJurisdiction(CommonConstants.ST_CIC_JURISDICTION);
        fgmAppDetail.setCaseTypeOfApplication(List.of(CASE_DATA_CIC_ID));
        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setSubmitEvent("citizen-prl-submit-dss-application");

        fgmAppDetail.setEventIds(eventsConfig);

    }

    @Test
    void shouldAddConfigurationToConfigBuilder() throws Exception {

        when(addSystemUpdateRole.addIfConfiguredForEnvironment(anyList()))
            .thenReturn(List.of(CITIZEN));

        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        cicSubmitCaseEvent.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(AppsUtil.getExactAppsDetailsByCaseType(appsConfig, CommonConstants.ST_CIC_CASE_TYPE).getEventIds()
                          .getSubmitEvent());
    }

    @Test
    void shouldSubmitEventThroughAboutToSubmit() throws IOException {
        final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        final CaseData caseBeforeData = mapper.readValue(caseDataJson, CaseData.class);

        final CaseDetails<CaseData, State> beforeCaseDetails = new CaseDetails<>();
        beforeCaseDetails.setData(caseBeforeData);
        beforeCaseDetails.setState(State.SUBMITTED);
        beforeCaseDetails.setId(TEST_CASE_ID);
        beforeCaseDetails.setCreatedDate(LOCAL_DATE_TIME);

        CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);
        caseDetails.setData(caseData);
        caseDetails.setState(State.SUBMITTED);
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setCreatedDate(LOCAL_DATE_TIME);

        caseDetails.getData().getApplicant().setEmailAddress(TEST_UPDATE_CASE_EMAIL_ADDRESS);

        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        cicSubmitCaseEvent.configure(configBuilder);

        AboutToStartOrSubmitResponse<Object, Object> submitResponseBuilder
            = AboutToStartOrSubmitResponse.builder().data(caseData).state(State.SUBMITTED).build();


        AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmitResponse = cicSubmitCaseEvent.aboutToSubmit(
            caseDetails,
            beforeCaseDetails
        );

        Assertions.assertEquals(aboutToSubmitResponse.getData(), caseDetails.getData());
        Assertions.assertEquals(aboutToSubmitResponse.getState(), caseDetails.getState());
        Assertions.assertNotEquals(submitResponseBuilder.getData(), beforeCaseDetails.getData());
        Assertions.assertEquals(submitResponseBuilder.getState(), caseDetails.getState());
    }
}
