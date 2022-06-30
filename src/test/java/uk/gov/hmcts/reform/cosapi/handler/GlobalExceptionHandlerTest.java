package uk.gov.hmcts.reform.cosapi.handler;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.exception.CaseCreateOrUpdateException;
import uk.gov.hmcts.reform.cosapi.exception.DocumentUploadOrDeleteException;

import static uk.gov.hmcts.reform.cosapi.util.TestConstant.TEST_URL;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_DELETE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.DOCUMENT_UPLOAD_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_CREATE_FAILURE_MSG;
import static uk.gov.hmcts.reform.cosapi.util.TestConstant.CASE_UPDATE_FAILURE_MSG;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void handleDocumentDeleteThroughHandler() {
        DocumentUploadOrDeleteException delException = new DocumentUploadOrDeleteException(
            DOCUMENT_DELETE_FAILURE_MSG,
            new RuntimeException()
        );

        ResponseEntity<?> responseDeleteHandler = globalExceptionHandler.handleDocumentException(delException);

        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            responseDeleteHandler.getStatusCode()
        );

        assertTrue(responseDeleteHandler.getBody().toString().contains(DOCUMENT_DELETE_FAILURE_MSG));
    }

    @Test
    void handleDocumentUploadThroughHandler() {
        DocumentUploadOrDeleteException updException = new DocumentUploadOrDeleteException(
            DOCUMENT_UPLOAD_FAILURE_MSG,
            new RuntimeException()
        );

        ResponseEntity<?> exceptionResponseHandler = globalExceptionHandler.handleDocumentException(updException);

        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            exceptionResponseHandler.getStatusCode()
        );

        assertTrue(exceptionResponseHandler.getBody().toString().contains(DOCUMENT_UPLOAD_FAILURE_MSG));
    }

    @Test
    void handleCreateCaseApiExceptionThroughExceptionHandler() {
        CaseCreateOrUpdateException updException = new CaseCreateOrUpdateException(
            CASE_CREATE_FAILURE_MSG,
            new RuntimeException()
        );

        ResponseEntity<?> exceptionResponseHandler = globalExceptionHandler.handleDocumentException(updException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponseHandler.getStatusCode());
        assertTrue(exceptionResponseHandler.getBody().toString().contains(CASE_CREATE_FAILURE_MSG));
    }

    @Test
    void handleUpdateCaseApiExceptionThroughExceptionHandler() {
        CaseCreateOrUpdateException updException = new CaseCreateOrUpdateException(
            CASE_UPDATE_FAILURE_MSG,
            new RuntimeException()
        );

        ResponseEntity<?> exceptionResponseHandler = globalExceptionHandler.handleDocumentException(updException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponseHandler.getStatusCode());
        assertTrue(exceptionResponseHandler.getBody().toString().contains(CASE_UPDATE_FAILURE_MSG));
    }

    @Test
    void handleBadRequestExceptionCausedByIllegalArgumentExceptionThroughExceptionHandler() {
        IllegalArgumentException illException = new IllegalArgumentException("Illegal Argument Exception Caught");

        ResponseEntity<?> exceptionResponseHandler = globalExceptionHandler.handleBadRequestException(illException);

        assertEquals(HttpStatus.BAD_REQUEST, exceptionResponseHandler.getStatusCode());
    }

    @Test
    void handleBadRequestExceptionCausedByNullPointerExceptionThroughExceptionHandler() {
        NullPointerException nullException = new NullPointerException("NullPointer Exception Caught");

        ResponseEntity<?> exceptionResponseHandler = globalExceptionHandler.handleBadRequestException(nullException);

        assertEquals(HttpStatus.BAD_REQUEST, exceptionResponseHandler.getStatusCode());
    }

    @Test
    void handleFeignExceptionServiceUnavailableExceptionThroughExceptionHandler() {

        final byte[] emptyBody = {};
        Request request = Request.create(Request.HttpMethod.GET, TEST_URL,
                                         new HashMap<>(), null, new RequestTemplate()
        );

        FeignException serviceUnavailableException = new FeignException.ServiceUnavailable(
            "Service unavailable",
            request,
            null,
            null
        );

        ResponseEntity<?> serviceUnavailableResponseHandler =
            globalExceptionHandler.handleFeignExceptionServiceUnavailableException(serviceUnavailableException);

        assertEquals(serviceUnavailableException.getMessage(), serviceUnavailableResponseHandler.getBody());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, serviceUnavailableResponseHandler.getStatusCode());
    }

    @Test
    void handleFeignExceptionUnauthorizedExceptionThroughExceptionHandler() {

        final byte[] emptyBody = {};
        Request request = Request.create(Request.HttpMethod.GET, TEST_URL,
                                         new HashMap<>(), null, new RequestTemplate()
        );

        FeignException unauthorizedException = new FeignException.Unauthorized(
            "Unauthorized message",
            request,
            null,
            null
        );

        ResponseEntity<?> unauthorizedResponseHandler =
            globalExceptionHandler.handleFeignExceptionUnauthorizedException(unauthorizedException);

        assertEquals(unauthorizedException.getMessage(), unauthorizedResponseHandler.getBody());

        assertEquals(HttpStatus.UNAUTHORIZED, unauthorizedResponseHandler.getStatusCode());
    }
}
