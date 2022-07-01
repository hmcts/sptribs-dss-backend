package uk.gov.hmcts.reform.cosapi.exception;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.controllers.DocumentManagementController;
import uk.gov.hmcts.reform.cosapi.model.DocumentInfo;
import uk.gov.hmcts.reform.cosapi.services.DocumentManagementService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FILE_FGM;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_FGM_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_TEST_AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_URL;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_DELETE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_UPLOAD_FAILURE_MSG;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class DocumentUploadOrDeleteExceptionTest {
    private DocumentInfo documentInfo;

    @InjectMocks
    private DocumentManagementController documentManagementController;

    @Mock
    DocumentManagementService documentManagementService;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testDeleteDocumentUploadOrDeleteException() throws Exception {
        documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_FGM_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_FGM).build();

        when(documentManagementService.deleteDocument(CASE_TEST_AUTHORIZATION, documentInfo.getDocumentId())).thenThrow(
            new DocumentUploadOrDeleteException(
                DOCUMENT_DELETE_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            documentManagementController.deleteDocument(CASE_TEST_AUTHORIZATION, documentInfo.getDocumentId());
        });
        assertTrue(exception.getMessage().contains(DOCUMENT_DELETE_FAILURE_MSG),
            String.valueOf(true));
    }

    @Test
    void testUpdateDocumentUploadOrDeleteException() throws Exception {
        documentInfo = DocumentInfo.builder()
            .documentId(CASE_DATA_FGM_ID)
            .url(TEST_URL)
            .fileName(CASE_DATA_FILE_FGM).build();

        when(documentManagementService.deleteDocument(
            CASE_TEST_AUTHORIZATION,
            documentInfo.getDocumentId()
        )).thenThrow(
            new DocumentUploadOrDeleteException(
                DOCUMENT_UPLOAD_FAILURE_MSG,
                new RuntimeException()
            ));

        Exception exception = assertThrows(Exception.class, () -> {
            documentManagementController.deleteDocument(CASE_TEST_AUTHORIZATION, documentInfo.getDocumentId());
        });
        assertTrue(
            exception.getMessage().contains(DOCUMENT_UPLOAD_FAILURE_MSG),
            String.valueOf(true)
        );
    }
}
