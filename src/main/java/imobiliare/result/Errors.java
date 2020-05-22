package imobiliare.result;

import imobiliare.enums.ElementValue;
import imobiliare.enums.PageType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Errors {

    ErrorType errorType;
    String value;
    ElementValue elementValue;
    String errorMessage;
    Exception exception;
    PageType pageType;
}
