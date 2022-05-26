package uk.gov.hmcts.reform.cosapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class CaseResponse {
    private String status;
    private Long id;
    private Map<String, Object> caseData;
}
