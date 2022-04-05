package uk.gov.hmcts.reform.adoption.document.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.document.DocumentType;

import java.time.LocalDate;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@Builder
@ToString
public class AdoptionDocument {

    @CCD(
        label = "Add content to be emailed",
        typeOverride = TextArea
    )
    private String documentEmailContent;

    @CCD(
        label = "Select your document",
        regex = ".pdf,.tif,.tiff,.jpg,.jpeg,.png"
    )
    private Document documentLink;

    @CCD(
        label = "Date added"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate documentDateAdded;

    @CCD(
        label = "Your comments",
        hint = "Any relevant information that the court should know about the document"
    )
    private String documentComment;

    @CCD(
        label = "File name",
        hint = "For your own reference, to make the document easier to find"
    )
    private String documentFileName;

    @CCD(
        label = "Select document type",
        typeOverride = FixedList,
        typeParameterOverride = "DocumentType"
    )
    private DocumentType documentType;

    @CCD(
        label = "File Id",
        hint = "DM Store file Id"
    )
    private String documentFileId;

    //Add handwritten constructor as a workaround for @JsonUnwrapped prefix issue
    @JsonCreator
    public AdoptionDocument(@JsonProperty("documentEmailContent") String documentEmailContent,
                            @JsonProperty("documentLink") Document documentLink,
                            @JsonProperty("documentDateAdded") LocalDate documentDateAdded,
                            @JsonProperty("documentComment") String documentComment,
                            @JsonProperty("documentFileName") String documentFileName,
                            @JsonProperty("documentType") DocumentType documentType,
                            @JsonProperty("documentFileId") String documentFileId) {
        this.documentEmailContent = documentEmailContent;
        this.documentLink = documentLink;
        this.documentDateAdded = documentDateAdded;
        this.documentComment = documentComment;
        this.documentFileName = documentFileName;
        this.documentType = documentType;
        this.documentFileId = documentFileId;
    }
}
