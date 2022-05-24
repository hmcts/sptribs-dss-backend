package uk.gov.hmcts.reform.cosapi.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.model.CaseResponse;
import uk.gov.hmcts.reform.cosapi.services.CaseManagementService;

@RestController
@RequestMapping(value = "/case/dss-orhestration")
public class CaseManagementController {

    @Autowired
    CaseManagementService caseManagementService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "Call CCD to create case")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "created"),
        @ApiResponse(code = 401, message = "Provided Authroization token is missing or invalid"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> createCase(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                        @RequestBody final CaseData caseData) {

        CaseResponse createdCase = caseManagementService.createCase(authorisation, caseData);
        return ResponseEntity.ok(createdCase);
    }

    @PutMapping(value = "/{caseId}/update")
    @ApiOperation(value = "Call CCD to create case")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "updated"),
        @ApiResponse(code = 401, message = "Provided Authroization token is missing or invalid"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 404, message = "Case Not found")

    })
    public ResponseEntity<?> updateCase(@PathVariable final Long caseId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                        @RequestBody final CaseData caseData) {

        CaseResponse updatedCase = caseManagementService.updateCase(authorisation, caseData, caseId);
        return ResponseEntity.ok(updatedCase);
    }
}
