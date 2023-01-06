package uk.gov.hmcts.reform.cosapi.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.common.config.AppsConfig;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;

import static uk.gov.hmcts.reform.cosapi.constants.CommonConstants.ST_CIC_CASE_TYPE;
import static uk.gov.hmcts.reform.cosapi.constants.CommonConstants.ST_CIC_JURISDICTION;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_DATA_CIC_ID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application.yaml")
@SuppressWarnings("PMD")
public class AppsUtilTest {

    @Autowired
    AppsConfig appsConfig;

    @Test
    void isValidCaseTypeOfApplicationTest() {
        CaseData a100CaseData = CaseData.builder().caseTypeOfApplication(CASE_DATA_CIC_ID).build();
        Assert.assertTrue(AppsUtil.isValidCaseTypeOfApplication(appsConfig, a100CaseData));
    }

    @Test
    void inValidCaseTypeOfApplicationTest() {
        CaseData a100CaseData = CaseData.builder().caseTypeOfApplication("dummy").build();
        Assert.assertFalse(AppsUtil.isValidCaseTypeOfApplication(appsConfig, a100CaseData));
    }

    @Test
    void validateExactAppDetailsTest() {
        CaseData cicCaseData = CaseData.builder().caseTypeOfApplication(CASE_DATA_CIC_ID).build();
        AppsConfig.AppsDetails appDetails = AppsUtil.getExactAppsDetails(appsConfig, cicCaseData);
        Assert.assertEquals(ST_CIC_CASE_TYPE, appDetails.getCaseType());
        Assert.assertEquals(ST_CIC_JURISDICTION, appDetails.getJurisdiction());
    }
}
