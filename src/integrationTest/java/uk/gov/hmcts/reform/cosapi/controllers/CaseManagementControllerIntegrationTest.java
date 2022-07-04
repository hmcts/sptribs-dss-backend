package uk.gov.hmcts.reform.cosapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.RequestInterceptor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.cosapi.edgecase.event.EventEnum;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.exception.CaseCreateOrUpdateException;
import uk.gov.hmcts.reform.cosapi.model.CaseResponse;
import uk.gov.hmcts.reform.cosapi.services.CaseManagementService;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static uk.gov.hmcts.reform.cosapi.common.config.ControllerConstants.AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.common.config.ControllerConstants.AUTHORIZATION_VALUE;
import static uk.gov.hmcts.reform.cosapi.util.TestFileUtil.loadJson;
import static uk.gov.hmcts.reform.cosapi.util.TestResourceUtil.expectedResponse;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SuppressWarnings("PMD")
public class CaseManagementControllerIntegrationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private RequestInterceptor requestInterceptor;

    @MockBean
    CaseManagementService caseManagementService;


    @BeforeEach
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldSuccessfullyCreateACase() throws Exception {
        String caseDataJson = loadJson("requests/create-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/create-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.createCase(anyString(), any(CaseData.class)))
            .thenReturn(caseResponse);

        String response = mockMvc.perform(post("/case/dss-orchestration/create")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        assertThatJson(response)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(
                json(
                    expectedResponse("classpath:response/create-case-response.json")
                )
            );
    }

    @Test
    public void createACaseWithException() throws Exception {
        String caseDataJson = loadJson("requests/create-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/create-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.createCase(anyString(), any(CaseData.class)))
            .thenThrow(new CaseCreateOrUpdateException("Error"));

        String response = mockMvc.perform(post("/case/dss-orchestration/create")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is5xxServerError())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals("Error", response);
    }

    @Test
    public void shouldSuccessfullyUpdateCase() throws Exception {
        String caseDataJson = loadJson("requests/update-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/create-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.updateCase(anyString(), any(EventEnum.class), any(CaseData.class), anyLong()))
            .thenReturn(caseResponse);

        String response = mockMvc.perform(put("/case/dss-orchestration/1/update")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .param("event", "UPDATE")
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        assertThatJson(response)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(
                json(
                    expectedResponse("classpath:response/update-case-response.json")
                )
            );
    }

    @Test
    public void updateCaseWithException() throws Exception {
        String caseDataJson = loadJson("requests/update-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/update-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.updateCase(anyString(), any(EventEnum.class), any(CaseData.class), anyLong()))
            .thenThrow(new CaseCreateOrUpdateException("Error"));

        String response = mockMvc.perform(put("/case/dss-orchestration/1/update")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .param("event", "UPDATE")
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is5xxServerError())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals("Error", response);
    }

    @Test
    public void shouldSuccessfullySubmitCase() throws Exception {
        String caseDataJson = loadJson("requests/update-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/create-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.updateCase(anyString(), any(EventEnum.class), any(CaseData.class), anyLong()))
            .thenReturn(caseResponse);

        String response = mockMvc.perform(put("/case/dss-orchestration/1/update")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .param("event", "SUBMIT")
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        assertThatJson(response)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(
                json(
                    expectedResponse("classpath:response/update-case-response.json")
                )
            );
    }

    @Test
    public void submitCaseWithException() throws Exception {
        String caseDataJson = loadJson("requests/update-case.json");
        CaseData caseData = OBJECT_MAPPER.readValue(caseDataJson, CaseData.class);

        String caseResponseStr = loadJson("response/update-case-response.json");
        CaseResponse caseResponse = OBJECT_MAPPER.readValue(caseResponseStr, new TypeReference<>() {
        });

        when(caseManagementService.updateCase(anyString(), any(EventEnum.class), any(CaseData.class), anyLong()))
            .thenThrow(new CaseCreateOrUpdateException("Error"));

        String response = mockMvc.perform(put("/case/dss-orchestration/1/update")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .param("event", "SUBMIT")
                                              .content(
                                                  OBJECT_MAPPER.writeValueAsString(
                                                      caseData
                                                  )
                                              )
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is5xxServerError())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals("Error", response);
    }
}
