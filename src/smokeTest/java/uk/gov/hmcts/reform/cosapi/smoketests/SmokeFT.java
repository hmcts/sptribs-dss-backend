package uk.gov.hmcts.reform.cosapi.smoketests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class SmokeFT {

    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8099"
        );


    @Test
    void shouldCheckApplicationHealthUp() {

        RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(targetInstance)
            .setRelaxedHTTPSValidation()
            .build();

        Response response = given(requestSpecification)
            .when()
            .get("/health")
            .then()
            .extract().response();

        assertThat(response.getBody().asString()).contains("UP");
    }
}
