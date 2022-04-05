package uk.gov.hmcts.reform.adoption.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "docmosis")
@Validated
@Getter
public class DocmosisTemplatesConfig {
    @NotNull
    private final Map<LanguagePreference, Map<String, String>> templates = new HashMap<>();
}
