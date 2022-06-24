package uk.gov.hmcts.reform.cosapi.services.ccd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.event.EventEnum;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.services.SystemUserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_C100;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_C100_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_USER;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CaseApiServiceTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final String TEST_CASE_REFERENCE = "123";

    @InjectMocks
    private CaseApiService caseApiService;

    @Mock
    AuthTokenGenerator authTokenGenerator;

    @Mock
    SystemUserService systemUserService;

    @Mock
    StartEventResponse eventRes;

    @Mock
    CoreCaseDataApi coreCaseDataApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testC100CreateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_C100);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();

        caseDataMap.put(TEST_CASE_REFERENCE, caseData);
        CaseDetails caseDetail = CaseDetails.builder().caseTypeId(CASE_DATA_C100_ID)
            .id(TEST_CASE_ID)
            .data(caseDataMap)
            .jurisdiction(CommonConstants.JURISDICTION)
            .build();

        String userId = TEST_USER;
        eventRes = StartEventResponse.builder()
            .eventId(CommonConstants.CREATE_CASE_EVENT_ID)
            .caseDetails(caseDetail)
            .token(TEST_AUTHORIZATION_TOKEN)
            .build();

        when(systemUserService.getUserId(CASE_TEST_AUTHORIZATION)).thenReturn(userId);

        when(authTokenGenerator.generate()).thenReturn(userId);

        when(coreCaseDataApi.startForCitizen(
            CASE_TEST_AUTHORIZATION,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            CommonConstants.CREATE_CASE_EVENT_ID
        )).thenReturn(eventRes);

        CaseDataContent caseDataContent = CaseDataContent.builder()
            .data(caseData)
            .event(Event.builder().id(CommonConstants.CREATE_CASE_EVENT_ID).build())
            .eventToken(TEST_AUTHORIZATION_TOKEN)
            .build();

        when(coreCaseDataApi.submitForCitizen(
            CASE_TEST_AUTHORIZATION,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            true,
            caseDataContent
        )).thenReturn(caseDetail);

        CaseDetails createCaseDetail = caseApiService.createCase(CASE_TEST_AUTHORIZATION, caseData);

        assertEquals(CASE_DATA_C100_ID, createCaseDetail.getCaseTypeId());
        assertEquals(createCaseDetail.getId(), caseDetail.getId());
        assertEquals(createCaseDetail.getCaseTypeId(), caseDetail.getCaseTypeId());
        assertEquals(createCaseDetail.getData(), caseDetail.getData());
        assertEquals(createCaseDetail.getData().get(TEST_CASE_REFERENCE), caseDataMap.get(TEST_CASE_REFERENCE));
    }

    @Test
    void testC100UpdateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_C100);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();

        caseDataMap.put(TEST_CASE_REFERENCE, caseData);
        CaseDetails caseDetail = CaseDetails.builder().caseTypeId(CASE_DATA_C100_ID)
            .id(TEST_CASE_ID)
            .data(caseDataMap)
            .jurisdiction(CommonConstants.JURISDICTION)
            .build();

        String userId = TEST_USER;
        eventRes = StartEventResponse.builder()
            .eventId(String.valueOf(Event.builder().id(CommonConstants.UPDATE_CASE_EVENT_ID)))
            .caseDetails(caseDetail)
            .token(TEST_AUTHORIZATION_TOKEN)
            .build();
        when(coreCaseDataApi.startEventForCitizen(
            CASE_TEST_AUTHORIZATION,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            String.valueOf(TEST_CASE_ID),
            CommonConstants.UPDATE_CASE_EVENT_ID
        )).thenReturn(eventRes);

        when(systemUserService.getUserId(CASE_TEST_AUTHORIZATION)).thenReturn(userId);

        when(authTokenGenerator.generate()).thenReturn(userId);

        when(coreCaseDataApi.startEventForCitizen(
            CASE_TEST_AUTHORIZATION,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            TEST_CASE_REFERENCE,
            CommonConstants.UPDATE_CASE_EVENT_ID
        )).thenReturn(eventRes);

        CaseDataContent caseDataContent = CaseDataContent.builder()
            .data(caseData)
            .event(Event.builder().id(CommonConstants.UPDATE_CASE_EVENT_ID).build())
            .eventToken(TEST_AUTHORIZATION_TOKEN)
            .build();

        when(coreCaseDataApi.submitEventForCitizen(
            CASE_TEST_AUTHORIZATION,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            TEST_CASE_REFERENCE,
            true,
            caseDataContent
        )).thenReturn(caseDetail);

        CaseDetails createCaseDetail = caseApiService.updateCase(
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            TEST_CASE_ID,
            caseData
        );

        assertEquals(CASE_DATA_C100_ID, createCaseDetail.getCaseTypeId());
        assertEquals(createCaseDetail.getId(), caseDetail.getId());
        assertEquals(createCaseDetail.getCaseTypeId(), caseDetail.getCaseTypeId());
        assertEquals(createCaseDetail.getData(), caseDetail.getData());
        assertEquals(createCaseDetail.getData().get(TEST_CASE_REFERENCE), caseDataMap.get(TEST_CASE_REFERENCE));
    }
}
