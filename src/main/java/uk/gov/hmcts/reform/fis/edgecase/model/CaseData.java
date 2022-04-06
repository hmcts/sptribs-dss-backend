package uk.gov.hmcts.reform.fis.edgecase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.fis.edgecase.model.access.CaseworkerAccess;
import uk.gov.hmcts.reform.fis.edgecase.model.access.DefaultAccess;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CaseData {

    @JsonUnwrapped(prefix = "applicant1")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant1 = new Applicant();

    @CCD(
        label = "hyphenatedCaseReference",
        access = {CaseworkerAccess.class}
    )
    private String hyphenatedCaseRef;

    @CCD(
        label = "Due Date",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonUnwrapped()
    @Builder.Default
    private Application application = new Application();


    @CCD(
        label = "Applicant can not upload",
        access = {DefaultAccess.class}
    )
    private String applicant1CannotUpload;
    @CCD(
        label = "Applicant can not upload",
        access = {DefaultAccess.class}
    )
    private YesOrNo findFamilyCourt;

    @CCD(
        label = "Family court name",
        access = {DefaultAccess.class}
    )
    private String familyCourtName;

    @CCD(
        label = "Family court email",
        access = {DefaultAccess.class}
    )
    private String familyCourtEmailId;

    @JsonIgnore
    public String formatCaseRef(long caseId) {
        String temp = String.format("%016d", caseId);
        return String.format(
            "%4s-%4s-%4s-%4s",
            temp.substring(0, 4),
            temp.substring(4, 8),
            temp.substring(8, 12),
            temp.substring(12, 16)
        );
    }
}
