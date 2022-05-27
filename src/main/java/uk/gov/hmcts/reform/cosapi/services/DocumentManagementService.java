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

    public DocumentResponse storeDocument(String authorization, MultipartFile file) {
        try {
            DocumentInfo document = caseDocumentApiService.storeDocument(authorization, file);
            log.info("Stored Doc Detail: " + document.toString());
            return DocumentResponse.builder().status("Success").document(document).build();

        } catch (Exception e) {
            log.error("Error while storing  document ." + e.getMessage());
            throw new DocuementUploadOrDeleteException("Failing while storing the document" + e.getMessage(), e);
        }
    }

    public DocumentResponse deleteDocument(String authorization, String documentId) {
        try {
           caseDocumentApiService.deleteDocument(authorization, documentId);
            log.info("document deleted successfully.." );
            return DocumentResponse.builder().status("Success").document(null).build();

        } catch (Exception e) {
            log.error("Error while deleting  document ." + e.getMessage());
            throw new DocuementUploadOrDeleteException("Failing while deleting the document" + e.getMessage(), e);
        }
    }
}
