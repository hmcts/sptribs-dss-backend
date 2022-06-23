package uk.gov.hmcts.reform.cosapi.services.ccd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.event.EventEnum;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.services.SystemUserService;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@SuppressWarnings("PMD")
public class CaseApiService {

    @Autowired
    CoreCaseDataApi coreCaseDataApi;

    @Autowired
    AuthTokenGenerator authTokenGenerator;

    @Autowired
    SystemUserService systemUserService;

    @Autowired
    AppsConfig appsConfig;

    public CaseDetails createCase(String authorization, CaseData caseData,
                                  AppsConfig.AppsDetails appsDetails) {

        String userId = systemUserService.getUserId(authorization);

        return coreCaseDataApi.submitForCitizen(
            authorization,
            authTokenGenerator.generate(),
            userId,
            appsDetails.getJurisdiction(),
            appsDetails.getCaseType(),
            true,
            getCaseDataContent(authorization, caseData, userId, appsDetails.getEventIds().getCreateEvent())
        );
    }

    public CaseDetails updateCase(String authorization, EventEnum eventEnum, Long caseId,
                                  CaseData caseData, AppsConfig.AppsDetails appsDetails) {

        String userId = systemUserService.getUserId(authorization);

        return coreCaseDataApi.submitEventForCitizen(
            authorization,
            authTokenGenerator.generate(),
            userId,
            appsDetails.getJurisdiction(),
            appsDetails.getCaseType(),
            String.valueOf(caseId),
            true,
            getCaseDataContent(authorization, caseData, eventEnum, userId,
                    String.valueOf(caseId), appsDetails.getEventIds())
        );
    }


    private CaseDataContent getCaseDataContent(String authorization, CaseData caseData, String userId, String eventId) {
        return CaseDataContent.builder()
            .data(caseData)
            .event(Event.builder().id(eventId).build())
            .eventToken(getEventToken(authorization, userId, eventId))
            .build();
    }

    private CaseDataContent getCaseDataContent(String authorization, CaseData caseData, EventEnum eventEnum,
                                               String userId, String caseId, AppsConfig.EventsConfig eventsConfig) {
        CaseDataContent.CaseDataContentBuilder builder = CaseDataContent.builder().data(caseData);
        if (eventEnum.getEventType().equalsIgnoreCase(EventEnum.UPDATE.getEventType())) {
            builder.event(Event.builder().id(eventsConfig.getUpdateEvent()).build())
                .eventToken(getEventTokenForUpdate(authorization, userId, eventsConfig.getUpdateEvent(),
                                                   caseId));
        } else if (eventEnum.getEventType().equalsIgnoreCase(EventEnum.SUBMIT.getEventType())) {
            builder.event(Event.builder().id(eventsConfig.getSubmitEvent()).build())
                .eventToken(getEventTokenForUpdate(authorization, userId, eventsConfig.getSubmitEvent(),
                                                   caseId));
        }

        return builder.build();
    }

    public String getEventToken(String authorization, String userId, String eventId) {
        StartEventResponse res = coreCaseDataApi.startForCitizen(authorization,
                                                                 authTokenGenerator.generate(),
                                                                 userId,
                                                                 CommonConstants.JURISDICTION,
                                                                 CommonConstants.CASE_TYPE,
                                                                 eventId);

        //This has to be removed
        log.info("Response of create event token: " + res.getToken());

        return nonNull(res) ? res.getToken() : null;
    }

    public String getEventTokenForUpdate(String authorization, String userId, String eventId, String caseId) {
        StartEventResponse res = coreCaseDataApi.startEventForCitizen(authorization,
                authTokenGenerator.generate(),
                userId,
                CommonConstants.JURISDICTION,
                CommonConstants.CASE_TYPE,
                caseId,
                eventId);

        //This has to be removed
        log.info("Response of update event token: " + res.getToken());

        return nonNull(res) ? res.getToken() : null;
    }
}
