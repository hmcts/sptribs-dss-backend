package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public final class ValidationUtil {

    public static final String EMPTY = " cannot be empty or null";
    public static final String YES = "YES";
    public static final String LESS_THAN_TEN_WEEKS_AGO = " can not be less than ten weeks ago.";

    private ValidationUtil() {
    }

    public static List<String> validateBasicCase(CaseData caseData) {
        return flattenLists(
            validateApplicant1(caseData),
            validatePlacementOrders(caseData.getPlacementOrders())
        );
    }

    private static List<String> validatePlacementOrders(List<ListValue<PlacementOrder>> placementOrders) {
        boolean emptyPlacementOrderNumber = nonNull(placementOrders) && placementOrders
            .stream().anyMatch(placementOrderListValue -> isEmpty(placementOrderListValue.getValue()
                                                                      .getPlacementOrderNumber()));
        return emptyPlacementOrderNumber ? List.of("PlacementOrderNumber" + EMPTY) : emptyList();
    }

    private static List<String> validateApplicant1(CaseData caseData) {
        return flattenLists(
            notNull(caseData.getApplicant1().getFirstName(), "Applicant1FirstName"),
            notNull(caseData.getApplicant1().getLastName(), "Applicant1LastName"),
            notNull(caseData.getApplicant1().getEmailAddress(), "Applicant1EmailAddress"),
            notNull(caseData.getApplicant1().getPhoneNumber(), "Applicant1PhoneNumber"));
    }

    public static List<String> validateDateChildMovedIn(LocalDate dateChildMovedIn, String field) {
        if (nonNull(dateChildMovedIn) && dateChildMovedIn.isAfter(LocalDate.now().minus(10, WEEKS))) {
            return List.of(field + LESS_THAN_TEN_WEEKS_AGO);
        }
        return emptyList();
    }

    private static List<String> hasStatementOfTruth(YesOrNo statementOfTruth, String message) {
        return YesOrNo.YES.equals(statementOfTruth) ? emptyList() : List.of(message);
    }

    public static List<String> notNull(Object value, String field) {
        return isEmpty(value) ? List.of(field + EMPTY) : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(toList());
    }
}
