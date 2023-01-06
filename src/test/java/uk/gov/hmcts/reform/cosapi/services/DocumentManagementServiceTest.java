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
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_CIC_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_CIC;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_DELETE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_UPLOAD_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.JSON_CONTENT_TYPE;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.JSON_FILE_TYPE;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.RESPONSE_STATUS_SUCCESS;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_URL;
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
    private AppsConfig.AppsDetails fgmAppDetail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testUploadFgmDocument() throws Exception {
        fgmAppDetail = new AppsConfig.AppsDetails();
        fgmAppDetail.setCaseType(CommonConstants.ST_CIC_CASE_TYPE);
        fgmAppDetail.setJurisdiction(CommonConstants.ST_CIC_JURISDICTION);
        fgmAppDetail.setCaseTypeOfApplication(List.of(CASE_DATA_CIC_ID));
        AppsConfig.EventsConfig eventsConfig = new AppsConfig.EventsConfig();
        eventsConfig.setCreateEvent("");

        fgmAppDetail.setEventIds(eventsConfig);

        DocumentInfo documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_CIC_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_CIC).build();

        when(appsConfig.getApps()).thenReturn(Arrays.asList(fgmAppDetail));

        Assertions.assertNotNull(fgmAppDetail);

        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);

        MockMultipartFile multipartFile = new MockMultipartFile(
            JSON_FILE_TYPE,
                CASE_DATA_FILE_CIC,
            JSON_CONTENT_TYPE,
            caseDataJson.getBytes()
        );

        when(caseDocumentApiService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            multipartFile,
            fgmAppDetail
        )).thenReturn(documentInfo);

        DocumentResponse testUploadResponse = (DocumentResponse) documentManagementService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            fgmAppDetail
                .getCaseTypeOfApplication()
                .stream()
                .filter(eachCase -> eachCase.equals(CASE_DATA_CIC_ID))
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
    void testUploadFgmDocumentFailedWithException() throws Exception {
        String caseDataJson = loadJson(CASE_DATA_FILE_CIC);

        MockMultipartFile multipartFile = new MockMultipartFile(
            JSON_FILE_TYPE,
                CASE_DATA_FILE_CIC,
            JSON_CONTENT_TYPE,
            caseDataJson.getBytes()
        );

        when(caseDocumentApiService.uploadDocument(
            CASE_TEST_AUTHORIZATION,
            multipartFile,
            fgmAppDetail
        )).thenThrow(
            new DocumentUploadOrDeleteException(
                DOCUMENT_UPLOAD_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            documentManagementService.uploadDocument(CASE_TEST_AUTHORIZATION, CASE_DATA_CIC_ID, multipartFile);
        });

        assertTrue(exception.getMessage().contains(DOCUMENT_UPLOAD_FAILURE_MSG));
    }

    @Test
    void testDeleteFgmDocument() {

        DocumentResponse testDeleteResponse = (DocumentResponse) documentManagementService.deleteDocument(
            CASE_TEST_AUTHORIZATION,
                CASE_DATA_CIC_ID
        );

        Assertions.assertNotNull(testDeleteResponse);
        Assertions.assertEquals(RESPONSE_STATUS_SUCCESS, testDeleteResponse.getStatus());
    }

    @Test
    void testDeleteFgmDocumentFailedWithException() throws Exception {
        DocumentInfo documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_CIC_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_CIC).build();

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
