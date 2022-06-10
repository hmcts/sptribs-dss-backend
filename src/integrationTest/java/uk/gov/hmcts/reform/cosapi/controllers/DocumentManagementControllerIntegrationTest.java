package uk.gov.hmcts.reform.cosapi.controllers;

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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.cosapi.exception.DocuementUploadOrDeleteException;
import uk.gov.hmcts.reform.cosapi.model.DocumentResponse;
import uk.gov.hmcts.reform.cosapi.services.DocumentManagementService;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static uk.gov.hmcts.reform.cosapi.common.config.ControllerConstants.AUTHORIZATION;
import static uk.gov.hmcts.reform.cosapi.common.config.ControllerConstants.AUTHORIZATION_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SuppressWarnings("PMD")
public class DocumentManagementControllerIntegrationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private RequestInterceptor requestInterceptor;

    @MockBean
    DocumentManagementService documentManagementService;


    @BeforeEach
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldSuccessfullyUploadDoc() throws Exception {

        DocumentResponse documentResponse = DocumentResponse.builder().build();
        when(documentManagementService.uploadDocument(anyString(), any(MultipartFile.class)))
            .thenReturn(documentResponse);

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Test".getBytes()
        );

        String response = mockMvc.perform(multipart("/doc/dss-orhestration/upload")
                                              .file(file)
                                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals(OBJECT_MAPPER.writeValueAsString(documentResponse), response);
    }

    @Test
    public void uploadDocWithException() throws Exception {

        DocumentResponse documentResponse = DocumentResponse.builder().build();
        when(documentManagementService.uploadDocument(anyString(), any(MultipartFile.class)))
            .thenThrow(new DocuementUploadOrDeleteException("Error", new Exception()));

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Test".getBytes()
        );

        String response = mockMvc.perform(multipart("/doc/dss-orhestration/upload")
                                              .file(file)
                                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is5xxServerError())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals("Error", response);
    }

    @Test
    public void shouldSuccessfullyDeleteDoc() throws Exception {

        DocumentResponse documentResponse = DocumentResponse.builder().build();
        when(documentManagementService.deleteDocument(anyString(), anyString()))
            .thenReturn(documentResponse);

        String response = mockMvc.perform(delete("/doc/dss-orhestration/123/delete")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals(OBJECT_MAPPER.writeValueAsString(documentResponse), response);
    }

    @Test
    public void deleteDocWithException() throws Exception {

        DocumentResponse documentResponse = DocumentResponse.builder().build();
        when(documentManagementService.deleteDocument(anyString(), anyString()))
            .thenThrow(new DocuementUploadOrDeleteException("Error", new Exception()));

        String response = mockMvc.perform(delete("/doc/dss-orhestration/123/delete")
                                              .contentType(APPLICATION_JSON_VALUE)
                                              .header(AUTHORIZATION, AUTHORIZATION_VALUE)
                                              .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().is5xxServerError())
            .andReturn()
            .getResponse()
            .getContentAsString(UTF_8);

        Assert.assertEquals("Error", response);
    }

}
