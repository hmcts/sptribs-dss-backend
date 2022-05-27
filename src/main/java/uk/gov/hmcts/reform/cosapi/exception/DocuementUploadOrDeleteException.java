package uk.gov.hmcts.reform.cosapi.exception;

public class DocuementUploadOrDeleteException extends RuntimeException {
    private static final long serialVersionUID = 7442994120484411078L;

    public DocuementUploadOrDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
