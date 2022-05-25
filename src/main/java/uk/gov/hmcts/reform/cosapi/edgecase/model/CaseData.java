package uk.gov.hmcts.reform.cosapi.edgecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.cosapi.common.MappableObject;
import uk.gov.hmcts.reform.cosapi.edgecase.model.access.DefaultAccess;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CaseData implements MappableObject {

    @JsonUnwrapped(prefix = "applicant")
    @Builder.Default
    @CCD(access = {DefaultAccess.class})
    private Applicant applicant = new Applicant();
}
