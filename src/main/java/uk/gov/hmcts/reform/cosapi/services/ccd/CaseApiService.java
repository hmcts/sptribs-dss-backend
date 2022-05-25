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
import uk.gov.hmcts.reform.cosapi.constants.CommonConstants;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.services.SystemUserService;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class CaseApiService {

    @Autowired
    CoreCaseDataApi coreCaseDataApi;

    @Autowired
    AuthTokenGenerator authTokenGenerator;

    @Autowired
    SystemUserService systemUserService;

    public CaseDetails createCase(String authorization, CaseData caseData) {

        String userId = systemUserService.getUserId(authorization);

        return coreCaseDataApi.submitForCitizen(
            authorization,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            true,
            getCaseDataContent(authorization, caseData, userId)
        );
    }

    public CaseDetails updateCase(String authorization, Long caseId,
                                  CaseData caseData) {

        String userId = systemUserService.getUserId(authorization);

        return coreCaseDataApi.submitEventForCitizen(
            authorization,
            authTokenGenerator.generate(),
            userId,
            CommonConstants.JURISDICTION,
            CommonConstants.CASE_TYPE,
            String.valueOf(caseId),
            true,
                getCaseDataContent(authorization, caseData, userId,
                        String.valueOf(caseId))
        );
    }


    private CaseDataContent getCaseDataContent(String authorization, CaseData caseData, String userId) {
        return CaseDataContent.builder()
            .data(caseData)
            .event(Event.builder().id(CommonConstants.CREATE_CASE_EVENT_ID).build())
            .eventToken(getEventToken(authorization, userId, CommonConstants.CREATE_CASE_EVENT_ID))
            .build();
    }

    private CaseDataContent getCaseDataContent(String authorization, CaseData caseData, String userId,
                                               String caseId) {
        return CaseDataContent.builder()
                .data(caseData)
                .event(Event.builder().id(CommonConstants.UPDATE_CASE_EVENT_ID).build())
                .eventToken(getEventTokenForUpdate(authorization, userId, CommonConstants.UPDATE_CASE_EVENT_ID, caseId))
                .build();
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
