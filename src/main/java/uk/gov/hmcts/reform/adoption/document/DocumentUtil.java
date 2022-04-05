package uk.gov.hmcts.reform.adoption.document;

import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import static java.time.format.DateTimeFormatter.ofPattern;


public final class DocumentUtil {

    public static final DateTimeFormatter FILE_NAME_DATE_TIME_FORMATTER = ofPattern("yyyy-MM-dd:HH:mm");

    private DocumentUtil() {
    }

    public static Document documentFrom(final DocumentInfo documentInfo) {
        return new Document(
            documentInfo.getUrl(),
            documentInfo.getFilename(),
            documentInfo.getBinaryUrl());
    }

    public static AdoptionDocument adoptionDocumentFrom(final DocumentInfo documentInfo,
                                                        final DocumentType documentType) {
        return AdoptionDocument
            .builder()
            .documentLink(documentFrom(documentInfo))
            .documentFileName(documentInfo.getFilename())
            .documentFileId(documentInfo.getFileId())
            .documentType(documentType)
            .build();
    }

    public static String formatDocumentName(
        final Long caseId,
        final String documentName,
        final LocalDateTime localDateTime
    ) {
        return new StringJoiner("-")
            .add(documentName)
            .add(String.valueOf(caseId))
            .add(localDateTime.format(FILE_NAME_DATE_TIME_FORMATTER))
            .toString();
    }
}
