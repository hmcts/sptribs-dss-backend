package uk.gov.hmcts.reform.cosapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResponse {
    private String status;
    private DocumentInfo document;

}
