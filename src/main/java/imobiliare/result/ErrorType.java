package imobiliare.result;

/**
 * Created by vancer at 5/22/2020
 */
public enum ErrorType {
    INVALID_VALUE ("The Value was invalid, like the price was too big or to small or NULL."),
    CASTING_EXCEPTION("The value could not be casted, for example I got a string value containing letters, and tried to cast it to a number"),
    ELEMENT_NOT_FOUND("WebDriver did not found the element that I was searching for, eather the Xpath is wrong or the element is just not there"),
    WAITING_TIMEOUT("Am facut wait pana apare un element, si acesta nu a aparut");

    private ErrorType(String description) {
        this.description = description;
    }

    String description;
}
