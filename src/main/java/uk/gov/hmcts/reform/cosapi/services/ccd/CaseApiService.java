package uk.gov.hmcts.reform.cosapi.services.ccd;

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
            getCaseDataContent(authorization, caseData, userId, CommonConstants.CREATE_CASE_EVENT_ID)
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
            getCaseDataContent(authorization, caseData, userId, CommonConstants.UPDATE_CASE_EVENT_ID)
        );
    }


    private CaseDataContent getCaseDataContent(String authorization, CaseData caseData, String userId,
                                               String eventId) {
        return CaseDataContent.builder()
            .data(caseData)
            .event(Event.builder().id(CommonConstants.CREATE_CASE_EVENT_ID).build())
            .eventToken(getEventToken(authorization, userId, eventId))
            .build();
    }

    public String getEventToken(String authorization, String userId, String eventId) {
        StartEventResponse res = coreCaseDataApi.startForCitizen(authorization,
                                                                 authTokenGenerator.generate(),
                                                                 userId,
                                                                 CommonConstants.JURISDICTION,
                                                                 CommonConstants.CASE_TYPE,
                                                                 eventId);

        return nonNull(res) ? res.getToken() : null;
    }
}
