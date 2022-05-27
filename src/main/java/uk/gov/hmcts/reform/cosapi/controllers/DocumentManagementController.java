package uk.gov.hmcts.reform.cosapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.cosapi.services.DocumentManagementService;

@RestController
@RequestMapping("/doc/dss-orhestration")
public class DocumentManagementController {

    @Autowired
    DocumentManagementService documentManagementService;

    @RequestMapping(
        value = "/upload",
        method = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Call CDAM to upload document")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "created"),
        @ApiResponse(code = 401, message = "Provided Authroization token is missing or invalid"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> uploadDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                        @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(documentManagementService.storeDocument(authorisation, file));
    }

    @DeleteMapping("/{documentId}/delete")
    @ApiOperation("Call CDAM to delete document")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "updated"),
        @ApiResponse(code = 401, message = "Provided Authroization token is missing or invalid"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 404, message = "Case Not found")

    })
    public ResponseEntity<?> deleteDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                        @PathVariable("documentId") String documentId) {

        return ResponseEntity.ok(documentManagementService.deleteDocument(authorisation, documentId));
    }
}
