package uk.gov.hmcts.reform.cosapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataClientAutoConfiguration;
import uk.gov.hmcts.reform.idam.client.IdamApi;

@SpringBootApplication(exclude = {CoreCaseDataClientAutoConfiguration.class},
    scanBasePackages = {"uk.gov.hmcts.ccd.sdk", "uk.gov.hmcts.reform.cosapi",
        "uk.gov.hmcts.reform.idam"})
@EnableFeignClients(
    clients = {
        IdamApi.class,
        ServiceAuthorisationApi.class,
    }
)
@EnableScheduling
@SuppressWarnings("HideUtilityClassConstructor")
@Slf4j
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
