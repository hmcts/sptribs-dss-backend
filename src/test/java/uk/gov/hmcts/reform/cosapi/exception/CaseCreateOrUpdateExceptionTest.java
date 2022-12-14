package uk.gov.hmcts.reform.cosapi.exception;

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
import uk.gov.hmcts.reform.cosapi.controllers.CaseManagementController;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.services.CaseManagementService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_CIC;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_CREATE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_UPDATE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CaseCreateOrUpdateExceptionTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private CaseManagementController caseManagementController;

    @Mock
    private CaseManagementService caseManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCaseDataCaseCreateUpdateException() throws Exception {
        String createCaseTestAuth = CASE_TEST_AUTHORIZATION;
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        when(caseManagementService.createCase(createCaseTestAuth, caseData))
            .thenThrow(new CaseCreateOrUpdateException(CASE_CREATE_FAILURE_MSG, new Throwable()));

        Exception exception = assertThrows(Exception.class, () -> {
            caseManagementController.createCase(createCaseTestAuth, caseData);
        });

        assertTrue(exception.getMessage().contains(CASE_CREATE_FAILURE_MSG), String.valueOf(true));
    }

    @Test
    void testUpdateCaseDataCaseCreateUpdateException() throws Exception {
        String updateCaseTestAuth = CASE_TEST_AUTHORIZATION;
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);
        CaseData caseData = mapper.readValue(caseDataJson, CaseData.class);

        when(caseManagementService.createCase(updateCaseTestAuth, caseData))
            .thenThrow(new CaseCreateOrUpdateException(CASE_UPDATE_FAILURE_MSG, new Throwable()));

        Exception exception = assertThrows(Exception.class, () -> {
            caseManagementController.createCase(updateCaseTestAuth, caseData);
        });

        assertTrue(exception.getMessage().contains(CASE_UPDATE_FAILURE_MSG), String.valueOf(true));
    }
}
