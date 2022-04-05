package uk.gov.hmcts.reform.fis.document;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.fis.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.fis.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.fis.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.fis.idam.IdamService;

import java.util.Map;

import static uk.gov.hmcts.reform.fis.document.DocumentUtil.adoptionDocumentFrom;
import static uk.gov.hmcts.reform.fis.document.DocumentUtil.documentFrom;

@Service
@Slf4j
public class CaseDataDocumentService {

    @Autowired
    private DocAssemblyService docAssemblyService;

    @Autowired
    private DocumentIdProvider documentIdProvider;

    @Autowired
    private IdamService idamService;

    public void renderDocumentAndUpdateCaseData(final CaseData caseData,
                                                final DocumentType documentType,
                                                final Map<String, Object> templateContent,
                                                final Long caseId,
                                                final String templateId,
                                                final LanguagePreference languagePreference,
                                                final String filename) {

        log.info("Rendering document request for templateId : {} case id: {}", templateId, caseId);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();

        final var documentInfo = docAssemblyService.renderDocument(
            templateContent,
            caseId,
            authorisation,
            templateId,
            languagePreference,
            filename
        );

        log.info("Adding document to case data for templateId : {} case id: {}", templateId, caseId);


        log.info("Document url: {}", documentInfo.getFileId());

        ListValue<AdoptionDocument> adoptionDocument = ListValue.<AdoptionDocument>builder()
            .id(documentIdProvider.documentId())
            .value(adoptionDocumentFrom(documentInfo, documentType))
            .build();

        caseData.addToDocumentsGenerated(adoptionDocument);
    }

    public Document renderDocument(final Map<String, Object> templateContent,
                                   final Long caseId,
                                   final String templateId,
                                   final LanguagePreference languagePreference,
                                   final String filename) {

        log.info("Rendering document request for templateId : {} ", templateId);

        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();

        final var documentInfo = docAssemblyService.renderDocument(
            templateContent,
            caseId,
            authorisation,
            templateId,
            languagePreference,
            filename
        );

        return documentFrom(documentInfo);
    }
}
