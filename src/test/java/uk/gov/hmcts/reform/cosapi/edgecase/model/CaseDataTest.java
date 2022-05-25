package uk.gov.hmcts.reform.cosapi.edgecase.model;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.cosapi.document.DocumentType;
import uk.gov.hmcts.reform.cosapi.document.model.EdgeCaseDocument;

import java.util.List;
import java.util.Set;

class CaseDataTest {
    @Mock
    Applicant applicant;

    @Mock
    Application application;

    @Mock
    List<ListValue<EdgeCaseDocument>> documentsGenerated;

    @Mock
    List<ListValue<EdgeCaseDocument>> applicantDocumentsUploaded;

    @Mock
    List<ListValue<EdgeCaseDocument>> documentsUploaded;

    @Mock
    EdgeCaseDocument edgeCaseDocument;

    @Mock
    Set<DocumentType> applicantCannotUploadSupportingDocument;

    @InjectMocks
    CaseData caseData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        applicant = new Applicant(
            "firstName",
            "lastName"
        );
        caseData.setApplicant(applicant);
    }

}

