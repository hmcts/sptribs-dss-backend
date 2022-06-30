package uk.gov.hmcts.reform.cosapi.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.exception.DocumentUploadOrDeleteException;
import uk.gov.hmcts.reform.cosapi.model.DocumentInfo;
import uk.gov.hmcts.reform.cosapi.model.DocumentResponse;
import uk.gov.hmcts.reform.cosapi.services.cdam.CaseDocumentApiService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_FGM;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_URL;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.JSON_CONTENT_TYPE;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.JSON_FILE_TYPE;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FGM_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.RESPONSE_STATUS_SUCCESS;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_UPLOAD_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_DELETE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application.yaml")
@ActiveProfiles("test")
class DocumentManagementServiceTest {
    @InjectMocks
    private DocumentManagementService documentManagementService;

    @Mock
    private CaseDocumentApiService caseDocumentApiService;

    @Mock
    private AppsConfig appsConfig;

    @Mock
    private AppsConfig.AppsDetails c100AppDetail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testUploadC100Document() throws Exception {
        c100AppDetail = new AppsConfig.AppsDetails();
        c100AppDetail.setCaseType(CommonConstants.PRL_CASE_TYPE);
        c100AppDetail.setJurisdiction(CommonConstants.PRL_JURISDICTION);
        c100AppDetail.setCaseTypeOfApplication(List.of(CASE_DATA_FGM_ID));
        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setCreateEvent("");

        c100AppDetail.setEventIds(eventsConfig);

        DocumentInfo documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_FGM_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_FGM).build();

        when(appsConfig.getApps()).thenReturn(Arrays.asList(c100AppDetail));

        Assertions.assertNotNull(c100AppDetail);

        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);

        MockMultipartFile multipartFile = new MockMultipartFile(
            JSON_FILE_TYPE,
            CASE_DATA_FILE_FGM,
            JSON_CONTENT_TYPE,
            caseDataJson.getBytes()
        );

        when(caseDocumentApiService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            multipartFile,
            c100AppDetail
        )).thenReturn(documentInfo);

        DocumentResponse testUploadResponse = (DocumentResponse) documentManagementService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            c100AppDetail
                .getCaseTypeOfApplication()
                .stream()
                .filter(eachCase -> eachCase.equals(CASE_DATA_FGM_ID))
                .findFirst()
                .get(),
            multipartFile
        );


        Assertions.assertNotNull(testUploadResponse);
        Assertions.assertEquals(documentInfo.getDocumentId(), testUploadResponse.getDocument().getDocumentId());
        Assertions.assertEquals(documentInfo.getFileName(), testUploadResponse.getDocument().getFileName());
        Assertions.assertEquals(documentInfo.getUrl(), testUploadResponse.getDocument().getUrl());
        Assertions.assertEquals(RESPONSE_STATUS_SUCCESS, testUploadResponse.getStatus());
    }

    @Test
    void testUploadC100DocumentFailedWithException() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_FGM);

        MockMultipartFile multipartFile = new MockMultipartFile(
            JSON_FILE_TYPE,
            CASE_DATA_FILE_FGM,
            JSON_CONTENT_TYPE,
            caseDataJson.getBytes()
        );

        when(caseDocumentApiService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            multipartFile,
            c100AppDetail
        )).thenThrow(
            new DocumentUploadOrDeleteException(
                DOCUMENT_UPLOAD_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            documentManagementService.uploadDocument(CASE_TEST_AUTHORIZATION, CASE_DATA_FGM_ID, multipartFile);
        });

        assertTrue(exception.getMessage().contains(DOCUMENT_UPLOAD_FAILURE_MSG));
    }

    @Test
    void testDeleteC100Document() {

        DocumentResponse testDeleteResponse = (DocumentResponse) documentManagementService.deleteDocument(
            CASE_TEST_AUTHORIZATION,
            CASE_DATA_FGM_ID
        );

        Assertions.assertNotNull(testDeleteResponse);
        Assertions.assertEquals(RESPONSE_STATUS_SUCCESS, testDeleteResponse.getStatus());
    }

    @Test
    void testDeleteC100DocumentFailedWithException() throws Exception {
        DocumentInfo documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_FGM_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_FGM).build();

        when(documentManagementService.deleteDocument(
            CASE_TEST_AUTHORIZATION,
            documentInfo.getDocumentId()
        )).thenThrow(
            new DocumentUploadOrDeleteException(
                DOCUMENT_DELETE_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            documentManagementService.deleteDocument(CASE_TEST_AUTHORIZATION, documentInfo.getDocumentId());
        });
        assertTrue(exception.getMessage().contains(DOCUMENT_DELETE_FAILURE_MSG));
    }
}
