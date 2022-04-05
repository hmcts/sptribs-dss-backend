package uk.gov.hmcts.reform.adoption.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;

@Service
@Slf4j
public class NotificationDispatcher {
    public void send(final ApplicantNotification applicantNotification, final CaseData caseData, final Long caseId)
        throws NotificationClientException, IOException {
        if (!caseData.getApplicant1().getEmailAddress().isEmpty()) {
            applicantNotification.sendToApplicants(caseData, caseId);
            try {
                applicantNotification.sendToLocalCourt(caseData, caseId);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
