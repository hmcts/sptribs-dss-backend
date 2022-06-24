package uk.gov.hmcts.reform.cosapi.document.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.common.config.DocmosisTemplatesConfig;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.cosapi.document.content.DocmosisTemplateConstants.SAMPLE_TEMPLATE;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.cosapi.edgecase.model.LanguagePreference.WELSH;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.ENGLISH_TEMPLATE_ID;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.WELSH_TEMPLATE_ID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class DocmosisTemplateProviderTest {

    @Mock
    private DocmosisTemplatesConfig docmosisTemplatesConfig;

    @InjectMocks
    private DocmosisTemplateProvider docmosisTemplateProvider;

    @Test
    void shouldReturnTemplateForEnglish() {

        mockDocmosisTemplateConfig();

        final String name = docmosisTemplateProvider.templateNameFor(SAMPLE_TEMPLATE, ENGLISH);

        assertThat(name, is(ENGLISH_TEMPLATE_ID));
    }

    @Test
    void shouldReturnTemplateForWelsh() {

        mockDocmosisTemplateConfig();

        final String name = docmosisTemplateProvider.templateNameFor(SAMPLE_TEMPLATE, WELSH);

        assertThat(name, is(WELSH_TEMPLATE_ID));
    }

    private void mockDocmosisTemplateConfig() {
        when(docmosisTemplatesConfig.getTemplates()).thenReturn(
            Map.of(
                ENGLISH, Map.of(
                    SAMPLE_TEMPLATE, ENGLISH_TEMPLATE_ID
                ),
                WELSH, Map.of(
                    SAMPLE_TEMPLATE, WELSH_TEMPLATE_ID
                )
            )
        );
    }
}
