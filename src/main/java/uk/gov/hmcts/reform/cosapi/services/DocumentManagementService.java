package uk.gov.hmcts.reform.cosapi.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.cosapi.exception.DocuementUploadOrDeleteException;
import uk.gov.hmcts.reform.cosapi.model.DocumentInfo;
import uk.gov.hmcts.reform.cosapi.model.DocumentResponse;
import uk.gov.hmcts.reform.cosapi.services.cdam.CaseDocumentApiService;

@Service
@Slf4j
public class DocumentManagementService {

    @Autowired
    CaseDocumentApiService caseDocumentApiService;

    public DocumentResponse uploadDocument(String authorization, MultipartFile file) {
        try {
            DocumentInfo document = caseDocumentApiService.uploadDocument(authorization, file);
            log.info("Stored Doc Detail: " + document.toString());
            return DocumentResponse.builder().status("Success").document(document).build();

        } catch (Exception e) {
            log.error("Error while uploading document ." + e.getMessage());
            throw new DocuementUploadOrDeleteException("Failing while uploading the document. The error message is " + e.getMessage(), e);
        }
    }

    public DocumentResponse deleteDocument(String authorization, String documentId) {
        try {
            caseDocumentApiService.deleteDocument(authorization, documentId);
            log.info("document deleted successfully..");
            return DocumentResponse.builder().status("Success").build();

        } catch (Exception e) {
            log.error("Error while deleting  document ." + e.getMessage());
            throw new DocuementUploadOrDeleteException("Failing while deleting the document. The error message is " + e.getMessage(), e);
        }
    }
}
