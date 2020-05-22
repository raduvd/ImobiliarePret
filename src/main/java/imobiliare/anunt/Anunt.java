package imobiliare.anunt;

import imobiliare.enums.ElementValue;
import imobiliare.enums.PageType;
import imobiliare.result.Result;
import imobiliare.result.ErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public abstract class Anunt {

    public static final By ELEMENT_PRICE_CURENCY = By.xpath(".//span[@itemprop= 'priceCurrency']");
    public static final By ELEMENT_PRET = By.xpath(".//div[@class= 'pret']/span");
    public static final By ELEMENT_PRET_WITH_EXTRA_DIV = By.xpath(".//div[@class= 'pret']/div/span");

    protected WebElement elementulAnunt;
    protected PageType pageType;
    protected String priceCurrency;
    protected BigDecimal pretPeMetruPatrat;
    protected BigDecimal pret;
    protected BigDecimal metriPatrati;

    public Anunt(WebElement elementulAnunt, PageType pageType) {
        this.pageType = pageType;
        this.elementulAnunt = elementulAnunt;
        this.priceCurrency = getPriceCurrencyFromElement();
    }

    /**
     * Validarile le fac si pentru a ma asigura ca datele ce le am sunt valide.
     * Spre exemplu un metruPatrat cu valoarea de 123000 nu e un metru patrat, nu e data valide.
     * <p>
     * Totodata fac aceste validari pentru a nu mi se strica calculele.
     * Spre exemplu un penthouse cu pretul de 1milion de dolari imi strica calculele de medie, astfel mai bine il scot.
     */
    public abstract boolean validateAnunt();

    public abstract BigDecimal getPretPeMetruPatratFromElement();

    protected boolean validatePriceCurrency(PageType pageType) {
        if (priceCurrency == null || !priceCurrency.equals("EUR")) {
            Result.add(ErrorType.INVALID_VALUE, priceCurrency, ElementValue.TEXT, "Currency non-valid", null, pageType);
            return false;
        }
        return true;
    }

    protected boolean validatePretPeMetruPatrat(PageType pageType) {
        if (pretPeMetruPatrat == null) {
            Result.add(ErrorType.INVALID_VALUE, pretPeMetruPatrat.toString(), null, "pretPeMetruPatrat null", null, pageType);
            return false;
        }
        return true;
    }

    protected Object getValueFromElement(By locator, By fallBackLocator, ElementValue elementValue) {

        WebElement element;

        try {
            try {
                element = elementulAnunt.findElement(locator);
            } catch (Exception e) {
                element = elementulAnunt.findElement(fallBackLocator);
            }
        } catch (Exception e) {
            Result.add(ErrorType.ELEMENT_NOT_FOUND, locator.toString() + "SI FALBACK: " + fallBackLocator.toString(), elementValue, e.getMessage(), e, PageType.GENERAL);
            return null;
        }

        String stringValue = element.getText();

        if (stringValue == null || stringValue.isEmpty())
            return null;

        stringValue = stringValue.split(" ")[0];

        switch (elementValue) {
            case PRET_CU_PUNCT:
                stringValue = stringValue.replace(".", "");
                return getBigDecimalOutOfString(stringValue, elementValue);
            case PRET_CU_VIRGULA:
                stringValue = stringValue.replace(".", "");
                stringValue = stringValue.replace(",", ".");
                return getBigDecimalOutOfString(stringValue, elementValue);
            case METRI_PATRATI:
                return getBigDecimalOutOfString(stringValue, elementValue);
            case TEXT:
            default:
                return stringValue;
        }
    }

    private BigDecimal getBigDecimalOutOfString(String stringValue, ElementValue elementValue) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(stringValue);
        } catch (Exception e) {
            Result.add(ErrorType.CASTING_EXCEPTION, stringValue, elementValue, e.getMessage(), e, PageType.GENERAL);
            return null;
        }
        return bigDecimal;
    }

    public String getPriceCurrencyFromElement() {
        return (String) getValueFromElement(ELEMENT_PRICE_CURENCY, ELEMENT_PRICE_CURENCY, ElementValue.TEXT);
    }

    public abstract BigDecimal getMetripatratiFromElement();

    public BigDecimal getPretFromElement() {
        return (BigDecimal) getValueFromElement(ELEMENT_PRET, ELEMENT_PRET_WITH_EXTRA_DIV, ElementValue.PRET_CU_PUNCT);
    }

    public Boolean validatePret(Double largerThen, Double smallerThen, PageType pageType) {

        if (pret == null
                || pret.doubleValue() < largerThen
                || pret.doubleValue() > smallerThen) {

            Result.add(ErrorType.INVALID_VALUE, pret == null ? null : pret.toString(), null, "pret non-valid", null, pageType);
            return false;
        }
        return true;
    }

    public Boolean validateMetriPatrati(Double largerThen, Double smallerThen, PageType pageType) {
        if (metriPatrati == null
                || metriPatrati.doubleValue() < largerThen
                || metriPatrati.doubleValue() > smallerThen) {
            Result.add(ErrorType.INVALID_VALUE, metriPatrati == null ? null : metriPatrati.toString(), null, "metruPatrat non-valid", null, pageType);
            return false;
        }
        return true;
    }
}
