package uk.gov.hmcts.reform.adoption.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocAssemblyResponse {
    private String renditionOutputLocation;

    public String getBinaryFilePath() {
        return renditionOutputLocation + "/binary";
    }

    public String getFileId() {
        return StringUtils.substringAfterLast(renditionOutputLocation, "/");
    }
}
