package uk.gov.hmcts.reform.cosapi.clients.cdam;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.apache.http.client.fluent.Request;
import uk.gov.hmcts.reform.cosapi.clients.CommonConstants;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "document_upload_api", port = "5006")
@PactFolder("pacts")
@SpringBootTest({  "document_upload_api:http://localhost:5006"})
public class DocumentUploadConsumerTest {

    @Pact(provider = "document_upload_api", consumer = "fis_cos")
    RequestResponsePact uploadDocument(PactDslWithProvider builder) throws JSONException, IOException {
        // @formatter:off

        PactDslJsonBody body = new PactDslJsonBody()
            .stringMatcher("caseTypeId", "PRLAPPS")
            .stringMatcher("jurisdictionId", "PRIVATELAW")
            .asBody();

        return builder
            .given("A request to upload a document")
            .uponReceiving("a request to upload a document with valid authorization")
            .method("POST")
            .headers(CommonConstants.SERVICE_AUTHORIZATION_HEADER, CommonConstants.someServiceAuthToken)
            .headers(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .path("/cases/documents")
            .willRespondWith()
            .status(HttpStatus.SC_OK)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "uploadDocument")
    public void verifyUploadDocument(MockServer mockServer) throws IOException {

        HttpResponse downloadDocumentResponse = Request.Post(mockServer.getUrl() + "/cases/documents")
            .addHeader(CommonConstants.SERVICE_AUTHORIZATION_HEADER, CommonConstants.someServiceAuthToken)
            .addHeader(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .execute().returnResponse();

        Assertions.assertEquals(200, downloadDocumentResponse.getStatusLine().getStatusCode());
    }
}
