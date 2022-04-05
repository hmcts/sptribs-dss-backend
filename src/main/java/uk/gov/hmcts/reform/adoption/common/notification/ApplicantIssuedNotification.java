package uk.gov.hmcts.reform.adoption.common.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.notification.ApplicantNotification;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.HashMap;

import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.TEST_EMAIL;

@Component
@Slf4j
public class ApplicantIssuedNotification implements ApplicantNotification {

    @Autowired
    NotificationService notificationService;

    @Override
    public void sendToApplicants(final CaseData caseData, final Long caseId) {
        final String email = caseData.getApplicant1().getEmail();
        final LanguagePreference languagePreference = caseData.getApplicant1().getLanguagePreference();
        notificationService.sendEmail(
            email,
            TEST_EMAIL, // TODO Update template here
            new HashMap<>(), // TODO replace with template vars once template is ready
            languagePreference
        );
    }
}
