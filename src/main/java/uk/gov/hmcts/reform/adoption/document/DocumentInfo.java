package uk.gov.hmcts.reform.adoption.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DocumentInfo {

    private final String url;

    private final String filename;

    private final String binaryUrl;

    private final String fileId;
}
