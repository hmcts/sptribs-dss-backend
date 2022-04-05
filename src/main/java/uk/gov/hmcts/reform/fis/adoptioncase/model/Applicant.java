package uk.gov.hmcts.reform.fis.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.fis.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Applicant {

    @CCD(label = "First name")
    private String firstName;

    @CCD(label = "Last name")
    private String lastName;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String email;

    @CCD(
        label = "Date of Birth",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(label = "Applicant Occupation")
    private String occupation;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String emailAddress;

    @CCD(label = "Applicant phoneNumber")
    private String phoneNumber;

    @CCD(label = "The court may want to use your email to serve you court orders. Are you happy to be served court "
        + "orders by email?")
    private YesOrNo contactDetailsConsent;

    @CCD(label = "Address1")
    private String address1;

    @CCD(label = "Address2")
    private String address2;

    @CCD(label = "Town")
    private String addressTown;

    @CCD(label = "Country")
    private String addressCountry;

    @CCD(label = "Post code")
    private String addressPostCode;

    @CCD(label = "contactDetails")
    private Set<ContactDetails> contactDetails;

    @CCD(label = "languagePreference")
    private LanguagePreference languagePreference;
}
