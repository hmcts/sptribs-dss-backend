package uk.gov.hmcts.reform.cosapi.edgecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.cosapi.common.MappableObject;
import uk.gov.hmcts.reform.cosapi.document.model.EdgeCaseDocument;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.DefaultAccess;

import java.util.List;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CaseData implements MappableObject {

    @CCD(
        label = "Named Applicant",
        access = {DefaultAccess.class}
    )
    private String namedApplicant;

    @CCD(
        label = "caseTypeOfApplication",
        access = {CaseworkerAccess.class}
    )
    private String caseTypeOfApplication;


    @JsonUnwrapped(prefix = "applicant")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant = new Applicant();

    @CCD(
            label = "Applicant application form uploaded documents",
            typeOverride = Collection,
            typeParameterOverride = "EdgeCaseDocument",
            access = {DefaultAccess.class}
    )
    private List<ListValue<EdgeCaseDocument>> applicantApplicationFormDocuments;

    @CCD(
            label = "Applicant additional uploaded documents",
            typeOverride = Collection,
            typeParameterOverride = "EdgeCaseDocument",
            access = {DefaultAccess.class}
    )
    private List<ListValue<EdgeCaseDocument>> applicantAdditionalDocuments;

    @CCD(
        label = "Subject Full Name",
        access = {DefaultAccess.class}
    )
    private String subjectFullName;

}
