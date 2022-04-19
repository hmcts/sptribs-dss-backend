package uk.gov.hmcts.reform.cosapi.edgecase.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.cosapi.document.DocumentType;
import uk.gov.hmcts.reform.cosapi.document.model.EdgeCaseDocument;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
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
            "lastName",
            "test1@gmail.com",
            LocalDate.of(2022, Month.APRIL, 6),
            "Software Engineer",
            "test2@gmail.com",
            "1234567890",
            YesOrNo.YES,
            "address1",
            "address2",
            "London",
            "UK",
            "E6 1AA",
            new HashSet<ContactDetails>(Arrays.asList(ContactDetails.EMAIL)),
            LanguagePreference.ENGLISH
        );
        caseData.setApplicant(applicant);
    }

    @Test
    void testFormatCaseRef() {
        String result = caseData.formatCaseRef(0L);
        Assertions.assertEquals("0000-0000-0000-0000", result);
    }

    @Test
    void testApplicant() {
        Applicant applicant = caseData.getApplicant();
        Assertions.assertNotNull(caseData);
        Assertions.assertNotNull(applicant);

        Assertions.assertEquals("firstName", applicant.getFirstName());
        Assertions.assertEquals("lastName", applicant.getLastName());
        Assertions.assertEquals("test1@gmail.com", applicant.getEmail());
        Assertions.assertEquals("email", applicant.getContactDetails().iterator().next().getLabel());
        Assertions.assertEquals(6, applicant.getDateOfBirth().getDayOfMonth());
        Assertions.assertEquals(Month.APRIL, applicant.getDateOfBirth().getMonth());
        Assertions.assertEquals(2022, applicant.getDateOfBirth().getYear());
        Assertions.assertEquals(YesOrNo.YES, applicant.getContactDetailsConsent());
        Assertions.assertEquals("Software Engineer", applicant.getOccupation());
        Assertions.assertEquals("test2@gmail.com", applicant.getEmailAddress());
        Assertions.assertEquals("1234567890", applicant.getPhoneNumber());
        Assertions.assertEquals("address1", applicant.getAddress1());
        Assertions.assertEquals("address2", applicant.getAddress2());
        Assertions.assertEquals("London", applicant.getAddressTown());
        Assertions.assertEquals("UK", applicant.getAddressCountry());
        Assertions.assertEquals("E6 1AA", applicant.getAddressPostCode());
    }

}

