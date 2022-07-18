package uk.gov.hmcts.reform.cosapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.event.EventEnum;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.exception.CaseCreateOrUpdateException;
import uk.gov.hmcts.reform.cosapi.model.CaseResponse;
import uk.gov.hmcts.reform.cosapi.services.ccd.CaseApiService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_FGM;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_FETCH_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_USER;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FGM_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.RESPONSE_STATUS_SUCCESS;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_CREATE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_UPDATE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_UPDATE_CASE_EMAIL_ADDRESS;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application.yaml")
@ActiveProfiles("test")
class CaseManagementServiceTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private CaseManagementService caseManagementService;

    @Mock
    private AppsConfig appsConfig;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private AppsConfig.AppsDetails fgmAppDetail;

    @Mock
    CaseApiService caseApiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        fgmAppDetail = new AppsConfig.AppsDetails();
        fgmAppDetail.setCaseType(CommonConstants.PRL_CASE_TYPE);
        fgmAppDetail.setJurisdiction(CommonConstants.PRL_JURISDICTION);
        fgmAppDetail.setCaseTypeOfApplication(List.of(CASE_DATA_FGM_ID));
    }

    @Test
    void testFgmCreateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();
        caseDataMap.put(CASE_DATA_FGM_ID, caseData);

        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setCreateEvent("citizen-prl-create-dss-application");

        fgmAppDetail.setEventIds(eventsConfig);
        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        assertNotNull(fgmAppDetail);

        when(authTokenGenerator.generate()).thenReturn(TEST_USER);

        CaseDetails caseDetail = CaseDetails.builder().caseTypeId(CASE_DATA_FGM_ID)
            .id(TEST_CASE_ID)
            .jurisdiction(CommonConstants.PRL_JURISDICTION)
            .data(caseDataMap)
            .build();

        when(caseApiService.createCase(CASE_TEST_AUTHORIZATION, caseData, fgmAppDetail)).thenReturn(caseDetail);

        CaseResponse caseResponse = CaseResponse.builder().caseData(caseDataMap).build();

        CaseResponse createCaseResponse = caseManagementService.createCase(CASE_TEST_AUTHORIZATION, caseData);
        assertEquals(createCaseResponse.getCaseData(), caseResponse.getCaseData());
        assertEquals(createCaseResponse.getId(), caseDetail.getId());
        assertTrue(createCaseResponse.getCaseData().containsKey(CASE_DATA_FGM_ID));

        CaseData caseResponseData = (CaseData) createCaseResponse.getCaseData().get(CASE_DATA_FGM_ID);
        assertNotNull(createCaseResponse);
        assertEquals(
            createCaseResponse.getCaseData().get(CASE_DATA_FGM_ID),
            caseDetail.getData().get(CASE_DATA_FGM_ID)
        );
        assertEquals(caseResponseData.getNamedApplicant(), caseData.getNamedApplicant());
        assertEquals(caseResponseData.getCaseTypeOfApplication(), caseData.getCaseTypeOfApplication());
        assertEquals(caseResponseData.getApplicant(), caseData.getApplicant());
        assertEquals(RESPONSE_STATUS_SUCCESS, createCaseResponse.getStatus());
    }

    @Test
    void testCreateCaseFgmFailedWithCaseCreateUpdateException() throws Exception {
        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setCreateEvent("citizen-prl-create-dss-application");

        fgmAppDetail.setEventIds(eventsConfig);
        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        assertNotNull(fgmAppDetail);

        when(authTokenGenerator.generate()).thenReturn(TEST_USER);

        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);

        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        when(caseApiService.createCase(CASE_TEST_AUTHORIZATION, caseData, fgmAppDetail)).thenThrow(
            new CaseCreateOrUpdateException(
                CASE_CREATE_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            caseManagementService.createCase(CASE_TEST_AUTHORIZATION, caseData);
        });

        assertTrue(exception.getMessage().contains(CASE_CREATE_FAILURE_MSG));
    }

    @Test
    void testFgmUpdateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setUpdateEvent("citizen-prl-update-dss-application");

        fgmAppDetail.setEventIds(eventsConfig);

        String origEmailAddress = caseData.getApplicant().getEmailAddress();
        caseData.getApplicant().setEmailAddress(TEST_UPDATE_CASE_EMAIL_ADDRESS);
        assertNotEquals(caseData.getApplicant().getEmailAddress(), origEmailAddress);

        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        assertNotNull(fgmAppDetail);

        when(authTokenGenerator.generate()).thenReturn(TEST_USER);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();
        caseDataMap.put(CASE_DATA_FGM_ID, caseData);

        CaseDetails caseDetail = CaseDetails.builder().caseTypeId(CASE_DATA_FGM_ID)
            .id(TEST_CASE_ID)
            .data(caseDataMap)
            .build();

        when(caseApiService.updateCase(
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            TEST_CASE_ID,
            caseData,
            fgmAppDetail
        )).thenReturn(caseDetail);

        CaseResponse updateCaseResponse = caseManagementService.updateCase(
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            caseData,
            TEST_CASE_ID
        );
        assertEquals(updateCaseResponse.getId(), caseDetail.getId());
        assertTrue(updateCaseResponse.getCaseData().containsKey(CASE_DATA_FGM_ID));


        CaseData caseResponseData = (CaseData) updateCaseResponse.getCaseData().get(CASE_DATA_FGM_ID);

        assertNotEquals(caseResponseData.getApplicant().getEmailAddress(), origEmailAddress);

        assertNotNull(updateCaseResponse);

        assertEquals(
            updateCaseResponse.getCaseData().get(CASE_DATA_FGM_ID),
            caseDetail.getData().get(CASE_DATA_FGM_ID)
        );
        assertEquals(caseResponseData.getNamedApplicant(), caseData.getNamedApplicant());
        assertEquals(caseResponseData.getCaseTypeOfApplication(), caseData.getCaseTypeOfApplication());
        assertEquals(caseResponseData.getApplicant().getEmailAddress(), caseData.getApplicant().getEmailAddress());
        assertEquals(RESPONSE_STATUS_SUCCESS, updateCaseResponse.getStatus());
    }

    @Test
    void testUpdateCaseFgmFailedWithCaseCreateUpdateException() throws Exception {
        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setUpdateEvent("citizen-prl-update-dss-application");

        fgmAppDetail.setEventIds(eventsConfig);
        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        assertNotNull(fgmAppDetail);

        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);

        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        when(authTokenGenerator.generate()).thenReturn(TEST_USER);
        when(caseApiService.updateCase(
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            TEST_CASE_ID,
            caseData,
            fgmAppDetail
        )).thenThrow(
            new CaseCreateOrUpdateException(
                CASE_UPDATE_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            caseManagementService.updateCase(CASE_TEST_AUTHORIZATION, EventEnum.UPDATE, caseData, TEST_CASE_ID);
        });

        assertTrue(exception.getMessage().contains(CASE_UPDATE_FAILURE_MSG));
    }

    @Test
    void testFetchCaseDetail() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        String origEmailAddress = caseData.getApplicant().getEmailAddress();
        caseData.getApplicant().setEmailAddress(TEST_UPDATE_CASE_EMAIL_ADDRESS);
        assertNotEquals(caseData.getApplicant().getEmailAddress(), origEmailAddress);

        when(authTokenGenerator.generate()).thenReturn(TEST_USER);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();
        caseDataMap.put(CASE_DATA_FGM_ID, caseData);

        CaseDetails caseDetail = CaseDetails.builder()
            .id(TEST_CASE_ID)
            .data(caseDataMap)
            .build();

        when(caseApiService.getCaseDetails(CASE_TEST_AUTHORIZATION,TEST_CASE_ID))
            .thenReturn(caseDetail);

        CaseResponse fetchCaseResponse = caseManagementService.fetchCaseDetails(
            CASE_TEST_AUTHORIZATION,
            TEST_CASE_ID);

        assertEquals(fetchCaseResponse.getId(), caseDetail.getId());
        assertEquals(RESPONSE_STATUS_SUCCESS, fetchCaseResponse.getStatus());


    }

    @Test
    void testFetchCaseDetailsWithException() throws Exception {

        when(caseApiService.getCaseDetails(CASE_TEST_AUTHORIZATION,TEST_CASE_ID))
            .thenThrow(new CaseCreateOrUpdateException(
                CASE_FETCH_FAILURE_MSG, new RuntimeException()
            ));

        Exception ex = assertThrows(Exception.class, () -> {
            caseManagementService.fetchCaseDetails(CASE_TEST_AUTHORIZATION,TEST_CASE_ID);
        });

        assertTrue(ex.getMessage().contains(CASE_FETCH_FAILURE_MSG));

    }

}
