package uk.gov.hmcts.reform.cosapi.util;

import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;

public class AppsUtil {

    public static boolean isValidCaseTypeOfApplication(AppsConfig appsConfig, CaseData caseData) {
        return caseData.getCaseTypeOfApplication() != null && appsConfig.getApps().stream()
            .anyMatch(eachApps -> eachApps.getCaseTypeOfApplication().contains(caseData.getCaseTypeOfApplication()));
    }

    public static AppsConfig.AppsDetails getExactAppsDetails(AppsConfig appsConfig, CaseData caseData) {
       return appsConfig.getApps().stream()
            .filter(eachApps -> eachApps.getCaseTypeOfApplication().contains(caseData.getCaseTypeOfApplication()))
            .findFirst().get();
    }
}
