package uk.gov.hmcts.reform.cosapi.clients.cdam;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.cosapi.clients.CommonConstants;

import java.io.IOException;


@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "document_delete_api", port = "5007")
@PactFolder("pacts")
@SpringBootTest({  "document_delete_api:http://localhost:5007"})
public class DocumentDeleteConsumerTest {


    @Pact(provider = "document_delete_api", consumer = "sptribs_cos")
    RequestResponsePact deleteDocument(PactDslWithProvider builder) throws JSONException, IOException {
        // @formatter:off

        PactDslJsonBody body = new PactDslJsonBody()
            .stringMatcher("caseTypeId", "CriminalInjuriesCompensation")
            .stringMatcher("jurisdictionId", "ST_CIC")
            .asBody();

        return builder
            .given("A request to delete a document")
            .uponReceiving("a request to delete a document with valid authorization")
            .method("POST")
            .headers(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .path("/" + CommonConstants.someDocumentId + "/documents")
            .willRespondWith()
            .status(HttpStatus.SC_OK)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "deleteDocument")
    public void verifyDeleteDocument(MockServer mockServer) throws IOException {

        HttpResponse downloadDocumentResponse = Request.Post(mockServer.getUrl()
                                      + "/" + CommonConstants.someDocumentId  + "/documents")
            .addHeader(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .execute().returnResponse();

        Assertions.assertEquals(200, downloadDocumentResponse.getStatusLine().getStatusCode());
    }


}
