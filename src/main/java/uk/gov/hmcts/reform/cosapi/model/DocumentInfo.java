package uk.gov.hmcts.reform.cosapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentInfo {
    private String url;
    private String mimeType;
    private String createdOn;
    private String hashToken;
    private String binaryUrl;
}
