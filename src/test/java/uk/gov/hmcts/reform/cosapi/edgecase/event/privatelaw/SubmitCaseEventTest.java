package uk.gov.hmcts.reform.cosapi.edgecase.event.privatelaw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.createCaseDataConfigBuilder;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.getEventsFrom;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_C100;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.LOCAL_DATE_TIME;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_UPDATE_CASE_EMAIL_ADDRESS;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SubmitCaseEventTest {
    final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

    @InjectMocks
    private SubmitCaseEvent submitCaseEvent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        submitCaseEvent.configure(configBuilder);
    }

//    @Test
//    void shouldAddConfigurationToConfigBuilder() throws Exception {
//
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getId)
//            .contains(SUBMIT_CASE_EVENT_ID);
//    }
//
//    @Test
//    void shouldSubmitEventThroughAboutToSubmit() throws IOException {
//        final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
//        String caseDataJson = loadJson(CASE_DATA_FILE_C100);
//        final CaseData caseBeforeData = mapper.readValue(caseDataJson, CaseData.class);
//
//        final CaseDetails<CaseData, State> beforeCaseDetails = new CaseDetails<>();
//        beforeCaseDetails.setData(caseBeforeData);
//        beforeCaseDetails.setState(State.SUBMITTED);
//        beforeCaseDetails.setId(TEST_CASE_ID);
//        beforeCaseDetails.setCreatedDate(LOCAL_DATE_TIME);
//
//        CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
//        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);
//        caseDetails.setData(caseData);
//        caseDetails.setState(State.SUBMITTED);
//        caseDetails.setId(TEST_CASE_ID);
//        caseDetails.setCreatedDate(LOCAL_DATE_TIME);
//
//        caseDetails.getData().getApplicant().setEmailAddress(TEST_UPDATE_CASE_EMAIL_ADDRESS);
//
//        AboutToStartOrSubmitResponse<Object, Object> submitResponseBuilder
//            = AboutToStartOrSubmitResponse.builder().data(caseData).state(State.SUBMITTED).build();
//
//
//        AboutToStartOrSubmitResponse aboutToSubmitResponse = submitCaseEvent.aboutToSubmit(
//            caseDetails,
//            beforeCaseDetails
//        );
//
//        Assertions.assertEquals(aboutToSubmitResponse.getData(), caseDetails.getData());
//        Assertions.assertEquals(aboutToSubmitResponse.getState(), caseDetails.getState());
//        Assertions.assertNotEquals(submitResponseBuilder.getData(), beforeCaseDetails.getData());
//        Assertions.assertEquals(submitResponseBuilder.getState(), caseDetails.getState());
//    }
}
