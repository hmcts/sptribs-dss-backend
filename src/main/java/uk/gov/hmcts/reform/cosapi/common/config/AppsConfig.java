package uk.gov.hmcts.reform.cosapi.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "caseinfo")
@Data
public class AppsConfig {
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
