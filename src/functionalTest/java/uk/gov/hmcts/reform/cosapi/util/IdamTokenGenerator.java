package uk.gov.hmcts.reform.cosapi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

@TestPropertySource("classpath:application.yaml")
@Service
public class IdamTokenGenerator {

    @Value("${idam.systemupdate.username}")
    private String systemUpdateUserName;

    @Value("${idam.systemupdate.password}")
    private String systemUpdatePassword;

    @Autowired
    private IdamClient idamClient;

    public String generateIdamTokenForCitizen() {
        return idamClient.getAccessToken(systemUpdateUserName, systemUpdatePassword);
    }

    public String generateIdamTokenForUser(String username, String password) {
        return idamClient.getAccessToken(username, password);
    }

    public UserDetails getUserDetailsFor(final String token) {
        return idamClient.getUserDetails(token);
    }
}
