package uk.gov.hmcts.reform.fis.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class S2sAuthServiceTest {

    @Mock
    private AuthTokenValidator authTokenValidator;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @InjectMocks
    private S2sAuthService s2sAuthService;

    @Test
    void tokenValidator() {
        Mockito.doNothing().when(authTokenValidator).validate(Mockito.anyString());
        s2sAuthService.tokenValidator(Mockito.anyString());
        Mockito.verify(authTokenValidator, times(1)).validate(Mockito.anyString());
    }

    @Test
    void serviceAuthTokenGenerator() {
        Mockito.when(authTokenGenerator.generate()).thenReturn("TOKEN");
        String token = s2sAuthService.serviceAuthTokenGenerator();
        Assertions.assertEquals("TOKEN", token);
    }
}
