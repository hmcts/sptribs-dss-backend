package uk.gov.hmcts.reform.cosapi.idam;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.util.Collections;
import java.util.Map;

import static feign.Request.HttpMethod.GET;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class IdamServiceTest {
    public static final String SYSTEM_UPDATE_AUTH_TOKEN = "Bearer SystemUpdateAuthToken";
    public static final String SYSTEM_USER_USER_ID = "4";
    public static final String TEST_SYSTEM_UPDATE_USER_EMAIL = "testsystem@test.com";
    public static final String TEST_SYSTEM_USER_PASSWORD = "testpassword";
    public static final String INVALID_AUTH_TOKEN = "invalid_token";

    @InjectMocks
    private IdamService idamService;

    @Mock
    private IdamClient idamClient;

    @Test
    void shouldRetrieveUserWhenValidAuthorizationTokenIsPassed() {
        when(idamClient.getUserDetails(SYSTEM_UPDATE_AUTH_TOKEN))
            .thenReturn(userDetails());

        assertThatCode(() -> idamService.retrieveUser(SYSTEM_UPDATE_AUTH_TOKEN))
            .doesNotThrowAnyException();

        verify(idamClient).getUserDetails(SYSTEM_UPDATE_AUTH_TOKEN);
        verifyNoMoreInteractions(idamClient);
    }

    @Test
    void shouldThrowFeignUnauthorizedExceptionWhenInValidAuthorizationTokenIsPassed() {
        when(idamClient.getUserDetails("Bearer invalid_token"))
            .thenThrow(feignException(401, "Failed to retrieve Idam user"));

        Exception exception = assertThrows(Exception.class, () -> {
            idamClient.getUserDetails("Bearer invalid_token");
        });

        assertTrue(exception.getMessage().toString().contains("Failed to retrieve Idam user"));

        assertThatThrownBy(() -> idamService.retrieveUser(INVALID_AUTH_TOKEN))
            .isExactlyInstanceOf(FeignException.Unauthorized.class)
            .hasMessageContaining("Failed to retrieve Idam user");
    }

    @Test
    void shouldNotThrowExceptionAndRetrieveSystemUpdateUserSuccessfully() {
        setSystemUserCredentials();

        when(idamClient.getAccessToken(TEST_SYSTEM_UPDATE_USER_EMAIL, TEST_SYSTEM_USER_PASSWORD))
            .thenReturn(SYSTEM_UPDATE_AUTH_TOKEN);

        when(idamClient.getUserDetails(SYSTEM_UPDATE_AUTH_TOKEN))
            .thenReturn(userDetails());

        assertThatCode(() -> idamService.retrieveSystemUpdateUserDetails())
            .doesNotThrowAnyException();

        verify(idamClient).getAccessToken(TEST_SYSTEM_UPDATE_USER_EMAIL, TEST_SYSTEM_USER_PASSWORD);
        verify(idamClient).getUserDetails(SYSTEM_UPDATE_AUTH_TOKEN);
        verifyNoMoreInteractions(idamClient);
    }

    private void setSystemUserCredentials() {
        ReflectionTestUtils.setField(idamService, "systemUpdateUserName", TEST_SYSTEM_UPDATE_USER_EMAIL);
        ReflectionTestUtils.setField(idamService, "systemUpdatePassword", TEST_SYSTEM_USER_PASSWORD);
    }

    private UserDetails userDetails() {
        return UserDetails
            .builder()
            .id(SYSTEM_USER_USER_ID)
            .email(TEST_SYSTEM_UPDATE_USER_EMAIL)
            .build();
    }

    @NotNull
    private static FeignException feignException(int status, String reason) {
        byte[] emptyBody = {};
        Request request = Request.create(GET, EMPTY, Map.of(), emptyBody, UTF_8, null);

        return FeignException.errorStatus(
            "idamRequestFailed",
            Response.builder()
                .request(request)
                .status(status)
                .headers(Collections.emptyMap())
                .reason(reason)
                .build()
        );
    }
}
