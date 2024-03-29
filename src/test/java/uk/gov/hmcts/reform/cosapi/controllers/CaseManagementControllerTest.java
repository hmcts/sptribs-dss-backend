package uk.gov.hmcts.reform.cosapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.edgecase.event.EventEnum;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.model.CaseResponse;
import uk.gov.hmcts.reform.cosapi.services.CaseManagementService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_CIC_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_CIC;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_EMAIL_ADDRESS;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_CASE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_UPDATE_CASE_EMAIL_ADDRESS;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CaseManagementControllerTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private CaseManagementController caseManagementController;

    @Mock
    CaseManagementService caseManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCicCreateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();

        CaseResponse caseResponse = CaseResponse.builder().caseData(caseDataMap).build();

        when(caseManagementService.createCase(CASE_TEST_AUTHORIZATION, caseData)).thenReturn(caseResponse);

        ResponseEntity<?> createCaseResponse = caseManagementController.createCase(CASE_TEST_AUTHORIZATION, caseData);

        CaseResponse testResponse = (CaseResponse) createCaseResponse.getBody();

        assertNotNull(testResponse);
        assertEquals(HttpStatus.OK, createCaseResponse.getStatusCode());
    }

    @Test
    void testCicUpdateCaseData() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();

        caseDataMap.put(CASE_DATA_CIC_ID, caseData);
        CaseResponse caseResponse = CaseResponse.builder().caseData(caseDataMap).build();
        caseResponse.setId(TEST_CASE_ID);
        caseResponse.setStatus(null);

        when(caseManagementService.updateCase(CASE_TEST_AUTHORIZATION, EventEnum.UPDATE,
                                              caseData, TEST_CASE_ID)).thenReturn(caseResponse);

        ResponseEntity<?> preUpdateCaseResponse = caseManagementController.updateCase(
            TEST_CASE_ID,
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            caseData
        );

        CaseResponse testPreUpdResponse = (CaseResponse) preUpdateCaseResponse.getBody();
        assertEquals(TEST_CASE_EMAIL_ADDRESS, caseData.getSubjectEmailAddress());

        CaseData caseDataUpdate = (CaseData) testPreUpdResponse.getCaseData().get(CASE_DATA_CIC_ID);
        caseDataUpdate.setSubjectEmailAddress(TEST_UPDATE_CASE_EMAIL_ADDRESS);

        ResponseEntity<?> postUpdateCaseResponse = caseManagementController.updateCase(
            TEST_CASE_ID,
            CASE_TEST_AUTHORIZATION,
            EventEnum.UPDATE,
            caseDataUpdate
        );

        CaseResponse caseDataUpdateResponse = (CaseResponse) (postUpdateCaseResponse.getBody());

        CaseData caseDataUpdatedFromResponse = (CaseData) (caseDataUpdateResponse.getCaseData().get(CASE_DATA_CIC_ID));

        assertEquals(
            caseDataUpdatedFromResponse.getSubjectEmailAddress(),
            caseDataUpdate.getSubjectEmailAddress()
        );
        assertEquals(TEST_UPDATE_CASE_EMAIL_ADDRESS, caseDataUpdate.getSubjectEmailAddress());

        assertNotNull(testPreUpdResponse);
        assertEquals(HttpStatus.OK, postUpdateCaseResponse.getStatusCode());
    }

    @Test
    void testFetchCaseDetails() throws IOException {

        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        CaseData caseData = mapper.readValue(caseDataJson,CaseData.class);

        Map<String, Object> caseDataMap = new ConcurrentHashMap<>();
        caseDataMap.put(CASE_DATA_CIC_ID, caseData);

        CaseResponse caseResponse = CaseResponse.builder().caseData(caseDataMap).build();
        caseResponse.setId(TEST_CASE_ID);
        caseResponse.setStatus(null);

        when(caseManagementService.fetchCaseDetails(CASE_TEST_AUTHORIZATION,TEST_CASE_ID)).thenReturn(caseResponse);

        ResponseEntity<?> postFetchCaseResponse = caseManagementController.fetchCaseDetails(
            TEST_CASE_ID,
            CASE_TEST_AUTHORIZATION
        );

        CaseResponse caseDataFetchResponse = (CaseResponse) (postFetchCaseResponse.getBody());

        assertEquals(caseDataFetchResponse.getId(),caseResponse.getId());
        assertEquals(postFetchCaseResponse.getStatusCode(),HttpStatus.OK);

    }

}
