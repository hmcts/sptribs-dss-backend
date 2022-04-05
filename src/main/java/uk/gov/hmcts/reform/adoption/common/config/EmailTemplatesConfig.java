package uk.gov.hmcts.reform.adoption.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "uk.gov.notify.email")
@Validated
@Getter
public class EmailTemplatesConfig {
    @NotNull
    private final Map<LanguagePreference, Map<String, String>> templates = new ConcurrentHashMap<>();

    @NotNull
    private final Map<String, String> templateVars = new ConcurrentHashMap<>();
}
