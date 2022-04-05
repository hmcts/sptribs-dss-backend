package uk.gov.hmcts.reform.fis.document.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.fis.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.fis.common.config.DocmosisTemplatesConfig;


@Component
public class DocmosisTemplateProvider {

    @Autowired
    private DocmosisTemplatesConfig docmosisTemplatesConfig;

    public String templateNameFor(final String templateId, final LanguagePreference languagePreference) {
        return docmosisTemplatesConfig.getTemplates().get(languagePreference).get(templateId);
    }
}
