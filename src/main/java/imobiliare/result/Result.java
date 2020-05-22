package imobiliare.result;

import imobiliare.enums.ElementValue;
import imobiliare.enums.PageType;

import java.util.*;
import java.util.stream.Collectors;

import static imobiliare.WriteToExcel.PATH_TO_OUTPUT_FILE;

public class Result {

    private static int ANUNTURI_PER_PAGINA = 30;
    public static Integer NUMARUL_PAGINII = 0;
    public static Integer ANUNTURI_PROCESATE = 0;

    private static List<Errors> allErrorsList = new ArrayList<>();

    private static List<Errors> invalidValueList = new ArrayList<>();
    private static List<Errors> castingExceptionList = new ArrayList<>();
    private static List<Errors> elementNotFoundList = new ArrayList<>();
    private static List<Errors> waitingTimeoutList = new ArrayList<>();

    private Result() {
    }

    public static void add(ErrorType errorType, String value, ElementValue elementValue, String errorMessage, Exception exception, PageType impobilType) {
        if (value == null)
            value = "null";

        if (errorMessage.length() > 130) {
            errorMessage = errorMessage.substring(0, 129);
        }
        final Errors error = Errors.builder().
                errorType(errorType).
                value(value).
                elementValue(elementValue).
                errorMessage(errorMessage).
                exception(exception).
                pageType(impobilType).
                build();

        allErrorsList.add(error);

        switch (errorType) {
            case INVALID_VALUE:
                invalidValueList.add(error);
                break;
            case WAITING_TIMEOUT:
                waitingTimeoutList.add(error);
                break;
            case CASTING_EXCEPTION:
                castingExceptionList.add(error);
                break;
            case ELEMENT_NOT_FOUND:
                elementNotFoundList.add(error);
                break;
        }
    }

    public static void sumUpPrint() {
        System.out.println("..\nSUM-UP\n..\n");
        System.out.println("TREBUIA SA FIE " + NUMARUL_PAGINII * ANUNTURI_PER_PAGINA + " ANUNTURI PROCESATE.");
        System.out.println("MOMENTAN SUNT: " + ANUNTURI_PROCESATE + " ANUNTURI PROCESATE.");
        System.out.println("ACESTE INFORMATII DE MAI SUS AU FOST SCRISE IN: " + PATH_TO_OUTPUT_FILE);
        System.out.println("TOTAL number of errors: " + allErrorsList.size());
        System.out.println(ErrorType.INVALID_VALUE.name() + " - number of errors: " + invalidValueList.size() + " ---- description: " + ErrorType.INVALID_VALUE.description);
        System.out.println(ErrorType.WAITING_TIMEOUT.name() + " - number of errors: " + waitingTimeoutList.size() + " ---- description: " + ErrorType.WAITING_TIMEOUT.description);
        System.out.println(ErrorType.CASTING_EXCEPTION.name() + " - number of errors: " + castingExceptionList.size() + " ---- description: " + ErrorType.CASTING_EXCEPTION.description);
        System.out.println(ErrorType.ELEMENT_NOT_FOUND.name() + " - number of errors: " + elementNotFoundList.size() + " ---- description: " + ErrorType.ELEMENT_NOT_FOUND.description);
        final List<Errors> sortedAllErrorList = allErrorsList.stream().
                sorted(Comparator.comparing(Errors::getValue)).collect(Collectors.toList());

        System.out.println("..\nSHORT ERRORS\n..\n");
        sortedAllErrorList.forEach(e -> System.out.println(e.value + " - " + e.getErrorType() + " - " + e.getErrorMessage() + " - " + e.getPageType().name()));

        Set<Errors> uniqueValueErrors = new TreeSet<>(Comparator.comparing(Errors::getValue));
        uniqueValueErrors.addAll(sortedAllErrorList);

        System.out.println("..\nSTACK TRACES\n..\n");
        uniqueValueErrors.forEach(e -> {
            if (e.getException() != null) {
                System.out.println(e.value + " - " + e.getErrorType() + " - " + e.getErrorMessage() + " - " + e.getPageType().name());
                e.getException().printStackTrace();
            }
        });
    }
}
