package uk.gov.hmcts.reform.cosapi.exception;

public class CaseCreateOrUpdateException extends RuntimeException {

    public CaseCreateOrUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
