package uk.gov.hmcts.reform.cosapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.model.CaseResponse;
import uk.gov.hmcts.reform.cosapi.util.IdamTokenGenerator;

import static uk.gov.hmcts.reform.cosapi.util.FileUtil.loadJson;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
@SuppressWarnings("PMD")
public class CaseManagementControllerFunctionalTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${case.orchestration.service.base.uri}")
    private String baseUri;

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
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldSuccessfullyCreateACase() throws Exception {
        String caseResponseStr = loadJson("response/create-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        createCase().then().assertThat().statusCode(200);

    }

    private Response createCase() throws Exception {
        String caseDataJson = loadJson("requests/create-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        return request
            .header("Authorization", idamTokenGenerator.generateIdamTokenForCitizen())
            .body(caseDataJson)
            .when()
            .contentType("application/json")
            .post("/case/dss-orchestration/create");
    }


    @Test
    public void shouldSuccessfullyUpdateCase() throws Exception {
        String caseDataJson = loadJson("requests/update-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        Response response = createCase();
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(response.getBody().asString(), new TypeReference<>() {
        });

        RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance)
            .header("Authorization", idamTokenGenerator.generateIdamTokenForCitizen())
            .body(caseDataJson)
            .when()
            .contentType("application/json")
            .put(String.format("/case/dss-orchestration/%s/update", caseResponse.getId()))
            .then()
            .assertThat().statusCode(200);
    }
}
