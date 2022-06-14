package uk.gov.hmcts.reform.cosapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.cosapi.model.DocumentResponse;
import uk.gov.hmcts.reform.cosapi.util.IdamTokenGenerator;

import java.io.File;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
@SuppressWarnings("PMD")
public class DocumentManagementControllerFunctionalTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    protected IdamTokenGenerator idamTokenGenerator;

    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8099"
        );

    private final RequestSpecification request = RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldSuccessfullyUploadDocument() throws Exception {
        Response response = request
            .header("Authorization", idamTokenGenerator.generateIdamTokenForCitizen())
            .multiPart("file", new File("src/functionalTest/resources/Test.pdf"))
            //.multiPart("file", file, MediaType.ALL_VALUE)
            .when()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .post("/doc/dss-orhestration/upload");

        response.then().assertThat().statusCode(200);
        DocumentResponse res = objectMapper.readValue(response.getBody().asString(), DocumentResponse.class);

        Assert.assertEquals("Success", res.getStatus());
        Assert.assertNotNull(res.getDocument());
        Assert.assertEquals("Test.pdf", res.getDocument().getFileName());
    }

    @Test
    public void shouldSuccessfullyDeleteDocument() throws Exception {
        Response response = request
            .header("Authorization", idamTokenGenerator.generateIdamTokenForCitizen())
            .multiPart("file", new File("src/functionalTest/resources/Test.pdf"))
            .when()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .post("/doc/dss-orhestration/upload");

        DocumentResponse res = objectMapper.readValue(response.getBody().asString(), DocumentResponse.class);

        Response deleteResponse = RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance)
            .header("Authorization", idamTokenGenerator.generateIdamTokenForCitizen())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .delete(String.format("/doc/dss-orhestration/%s/delete", res.getDocument().getDocumentId()));

        deleteResponse.then().assertThat().statusCode(200);
        DocumentResponse delRes = objectMapper.readValue(deleteResponse.getBody().asString(), DocumentResponse.class);

        Assert.assertEquals("Success", delRes.getStatus());
    }
}
