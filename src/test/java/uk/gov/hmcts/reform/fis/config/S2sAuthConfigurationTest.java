package uk.gov.hmcts.reform.fis.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

@SpringBootTest
@Import(S2sAuthConfiguration.class)
class S2sAuthConfigurationTest {

    @Autowired
    private AuthTokenValidator authTokenValidator;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Test
    void tokenValidator() {
        Assertions.assertNotNull(authTokenValidator);
    }

    @Test
    void serviceAuthTokenGenerator() {
        Assertions.assertNotNull(authTokenGenerator);
    }
}
