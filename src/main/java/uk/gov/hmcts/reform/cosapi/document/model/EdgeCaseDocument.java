package uk.gov.hmcts.reform.cosapi.document.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@NoArgsConstructor
@ToString
public class EdgeCaseDocument {

    @CCD(
        label = "Select your document",
        regex = ".pdf,.tif,.tiff,.jpg,.jpeg,.png"
    )
    private uk.gov.hmcts.ccd.sdk.type.Document documentLink;
}
