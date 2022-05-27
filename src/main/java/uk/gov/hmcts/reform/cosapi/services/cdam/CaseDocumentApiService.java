package uk.gov.hmcts.reform.cosapi.services.cdam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.document.am.feign.CaseDocumentClient;
import uk.gov.hmcts.reform.ccd.document.am.model.Document;
import uk.gov.hmcts.reform.ccd.document.am.model.UploadResponse;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.model.DocumentInfo;

import java.util.Arrays;
import java.util.UUID;

@Service
public class CaseDocumentApiService {

    @Autowired
    AuthTokenGenerator authTokenGenerator;

    @Autowired
    CaseDocumentClient caseDocumentClient;

    public DocumentInfo storeDocument(String authorizationToken, MultipartFile file) {

        String serviceAuthToken = authTokenGenerator.generate();

        UploadResponse uploadResponse = caseDocumentClient.uploadDocuments(
            authorizationToken,
            serviceAuthToken,
            CommonConstants.CASE_TYPE,
            CommonConstants.JURISDICTION,
            Arrays.asList(file)
        );

        Document uploadedDocument = uploadResponse.getDocuments().get(0);

        return DocumentInfo.builder()
            .url(uploadedDocument.links.self.href)
            .mimeType(uploadedDocument.mimeType)
            .hashToken(uploadedDocument.hashToken)
            .binaryUrl(uploadedDocument.links.binary.href)
            .build();
    }

    public void deleteDocument(String authorizationToken, String documentId) {
        caseDocumentClient.deleteDocument(
            authorizationToken,
            authTokenGenerator.generate(),
            UUID.fromString(documentId),
            true
        );
    }
}

