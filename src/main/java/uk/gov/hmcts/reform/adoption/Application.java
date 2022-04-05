package uk.gov.hmcts.reform.adoption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.hmcts.reform.adoption.document.DocAssemblyClient;
import uk.gov.hmcts.reform.adoption.document.DocumentManagementClient;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.ccd.client.CaseAssignmentApi;
import uk.gov.hmcts.reform.ccd.client.CaseUserApi;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataClientAutoConfiguration;
import uk.gov.hmcts.reform.ccd.document.am.feign.CaseDocumentClientApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;

@SpringBootApplication(exclude = {CoreCaseDataClientAutoConfiguration.class},
    scanBasePackages = {"uk.gov.hmcts.ccd.sdk", "uk.gov.hmcts.reform.adoption", "uk.gov.hmcts.reform.ccd.document"})
@EnableFeignClients(
    clients = {
        IdamApi.class,
        ServiceAuthorisationApi.class,
        CaseUserApi.class,
        DocAssemblyClient.class,
        CoreCaseDataApi.class,
        CaseAssignmentApi.class,
        DocumentManagementClient.class,
        CaseDocumentClientApi.class
    }
)
@EnableScheduling
@EnableRetry
@EnableSwagger2
@SuppressWarnings("HideUtilityClassConstructor")
@Slf4j
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
