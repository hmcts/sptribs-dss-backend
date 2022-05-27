package uk.gov.hmcts.reform.cosapi.exception;

public class DocuementUploadOrDeleteException extends RuntimeException {

    public DocuementUploadOrDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
