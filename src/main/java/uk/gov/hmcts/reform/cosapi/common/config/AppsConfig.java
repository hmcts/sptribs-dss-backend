package uk.gov.hmcts.reform.cosapi.common.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "caseinfo")
@Data
@NoArgsConstructor
public class AppsConfig {

    private static AppsConfig appsCache = null;
    private static AppsConfig appsConfigInstance;

    public static AppsConfig getInstance() {
        if (appsConfigInstance == null) {
            appsConfigInstance = new AppsConfig();
        }
        if (appsCache == null) {
            appsCache = appsConfigInstance;
        }
        return appsConfigInstance;
    }

    List<AppsDetails> apps;

    @Data
    public static class EventsConfig {
        private String createEvent;
        private String updateEvent;
        private String submitEvent;
    }

    @Data
    public static class AppsDetails {
        private String jurisdiction;
        private String caseType;
        private List<String> caseTypeOfApplication;
        private EventsConfig eventIds;
    }

}
