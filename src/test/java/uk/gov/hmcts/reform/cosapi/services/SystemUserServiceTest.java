package uk.gov.hmcts.reform.cosapi.services;

import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SystemUserServiceTest {

    @Mock
    IdamClient idamClient;

    @InjectMocks
    SystemUserService systemUserService;

    String token = "";

    @BeforeEach
    public void setUp() {
        systemUserService = new SystemUserService(idamClient);
        token = RandomStringUtils.randomAlphanumeric(10);
    }

    @Test
    void shouldReturnSystemUserId() {
        UserInfo userInfo = UserInfo.builder()
            .uid(UUID.randomUUID().toString())
            .build();

        when(idamClient.getUserInfo(token)).thenReturn(userInfo);

        assertThat(userInfo.getUid()).isEqualTo(systemUserService.getUserId(token));
    }
}
