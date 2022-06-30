package uk.gov.hmcts.reform.cosapi.util;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TestConstant {

    public static final LocalDateTime LOCAL_DATE_TIME =
        LocalDateTime.of(2022, 2, 22, 16, 21);
    public static final Long TEST_CASE_ID = 123L;
    public static final String CASE_DATA_FGM_ID = "FGM";
    public static final String CASE_DATA_FILE_FGM = "FGMCaseData.json";
    public static final String CASE_TEST_AUTHORIZATION = "testAuth";
    public static final String CASE_CREATE_FAILURE_MSG = "Failing while creating the case ";
    public static final String CASE_UPDATE_FAILURE_MSG = "Failing while updating the case ";
    public static final String DOCUMENT_DELETE_FAILURE_MSG =
        "Failing while deleting the document. The error message is ";
    public static final String DOCUMENT_UPLOAD_FAILURE_MSG =
        "Failing while uploading the document. The error message is ";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String JSON_FILE_TYPE = "json";
    public static final String RESPONSE_STATUS_SUCCESS = "Success";
    public static final String TEST_AUTHORIZATION_TOKEN = "TestToken";
    public static final String TEST_CASE_EMAIL_ADDRESS = "test@test.com";
    public static final String TEST_RESOURCE_NOT_FOUND = "Could not find resource in path";
    public static final String TEST_UPDATE_CASE_EMAIL_ADDRESS = "testUpdate@test.com";
    public static final String TEST_URL = "TestUrl";
    public static final String TEST_USER = "TestUser";
    public static final UUID TEST_CASE_DATA_FILE_UUID = UUID.randomUUID();

    private TestConstant() {

    }
}
