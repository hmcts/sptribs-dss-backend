package uk.gov.hmcts.reform.cosapi.clients.ccd;

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
@PactTestFor(providerName = "ccd_submitForCitizen_api", port = "5001")
@PactFolder("pacts")
@SpringBootTest({  "ccd_submitForCitizen_api:http://localhost:5001"})
public class CaseManagementConsumerTest {



    @Pact(provider = "ccd_submitForCitizen_api", consumer = "fis_cos")
    RequestResponsePact createCase(PactDslWithProvider builder) throws JSONException, IOException {

        PactDslJsonBody body = new PactDslJsonBody()
            .stringMatcher("eventToken", "EventToken")
            .stringMatcher("case_reference", "CaseReference")
            .asBody();

        return builder
            .given("A request to create a case in CCD")
            .uponReceiving("a request to create a case in CCD with valid authorization")
            .method("POST")
            .headers(CommonConstants.SERVICE_AUTHORIZATION_HEADER, CommonConstants.someServiceAuthToken)
            .headers(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .headers("Content-Type","application/json")
            .path("/citizens/UserID/jurisdictions/jurisdictionId/case-types/caseType/cases")
            .willRespondWith()
            .status(HttpStatus.SC_OK)
            .body(createCaseResponse())
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createCase")
    public void verifyCreateCase(MockServer mockServer) throws IOException {

        HttpResponse downloadDocumentResponse = Request.Post(mockServer.getUrl()
                   + "/citizens/UserID/jurisdictions/jurisdictionId/case-types/caseType/cases")
            .addHeader(CommonConstants.SERVICE_AUTHORIZATION_HEADER, CommonConstants.someServiceAuthToken)
            .addHeader(CommonConstants.AUTHORIZATION_HEADER, CommonConstants.someAuthToken)
            .addHeader("Content-Type","application/json")
            .execute().returnResponse();
        Assertions.assertNotNull(downloadDocumentResponse);
        Assertions.assertEquals(200, downloadDocumentResponse.getStatusLine().getStatusCode());
    }

    private PactDslJsonBody createCaseResponse() {

        return new PactDslJsonBody()
            .stringType("case_type", "PRLAPPS")
            .stringType("callbackResponseStatus", "Success")
            .stringType("state", "State")
            .integerType("securityLevel",23)
            .date("createdDate","dd/mm/yyyy")
            .date("lastModified","dd/mm/yyyy");
    }
}
