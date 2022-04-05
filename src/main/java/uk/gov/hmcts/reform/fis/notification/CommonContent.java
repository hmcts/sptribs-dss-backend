package uk.gov.hmcts.reform.fis.notification;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.fis.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.fis.adoptioncase.model.CaseData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uk.gov.hmcts.reform.fis.notification.FormatUtil.formatId;

@Component
public class CommonContent {


    public static final String FIRST_NAME = "first name";
    public static final String LAST_NAME = "last name";
    public static final String SUBMISSION_RESPONSE_DATE = "date of response";
    public static final String APPLICATION_REFERENCE = "reference number";

    public Map<String, Object> mainTemplateVars(final CaseData caseData,
                                                final Long id,
                                                final Applicant applicant1) {
        Map<String, Object> templateVars = new ConcurrentHashMap<>();
        templateVars.put(APPLICATION_REFERENCE, id == null ? null : formatId(id));
        //        templateVars.put(IS_DIVORCE, caseData.isDivorce() ? YES : NO);
        //        templateVars.put(IS_DISSOLUTION, !caseData.isDivorce() ? YES : NO);
        templateVars.put(FIRST_NAME, applicant1.getFirstName());
        templateVars.put(LAST_NAME, applicant1.getLastName());
        //        templateVars.put(PARTNER, caseData.isDivorce() ? partner.getGender() == MALE ? "husband" :
        //        "wife" : "civil partner");
        //        templateVars.put(COURT_EMAIL,
        //            config.getTemplateVars().get(caseData.isDivorce() ? DIVORCE_COURT_EMAIL
        //            : DISSOLUTION_COURT_EMAIL));
        //        templateVars.put(SIGN_IN_URL,
        //            config.getTemplateVars().get(caseData.isDivorce() ? SIGN_IN_DIVORCE_URL
        //            : SIGN_IN_DISSOLUTION_URL));
        return templateVars;
    }
}
