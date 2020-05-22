package imobiliare.anunt;

import imobiliare.Calculations;
import imobiliare.enums.ElementValue;
import imobiliare.enums.PageType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class AnuntApartament extends Anunt {

    private double CONSIDER_ONLY_APARTMENTS_WITH_PRICE_LARGER_THEN = 10000;
    private double CONSIDER_ONLY_APARTMENTS_WITH_PRICE_SMALLER_THEN = 500000;
    private double CONSIDER_ONLY_APARTMENTS_WITH_MP_LARGER_THEN = 15;
    private double CONSIDER_ONLY_APARTMENTS_WITH_MP_SMALLER_THEN = 200;

    //sunt cu . in fata pentru a cauta exact in anuntul current
    public static final By ELEMENT_CARACTERISTICI = By.xpath(".//ul[@class= 'caracteristici']");
    public static final By ELEMENT_METRI_PATRATI = By.xpath(".//ul[@class= 'caracteristici']/li[2]");
    public static final By ELEMENT_METRI_PATRATI_FALLBACK = By.xpath(".//ul[@class= 'caracteristici']/li[1]");


    public AnuntApartament(WebElement elementulAnunt, PageType pageType) {
        super(elementulAnunt, pageType);
        this.pret = getPretFromElement();
        this.metriPatrati = getMetripatratiFromElement();
        this.pretPeMetruPatrat = getPretPeMetruPatratFromElement();
    }

    public BigDecimal getMetripatratiFromElement() {
        return (BigDecimal) getValueFromElement(ELEMENT_METRI_PATRATI, ELEMENT_METRI_PATRATI_FALLBACK, ElementValue.METRI_PATRATI);
    }

    public String getPriceCurrencyFromElement() {
        return (String) getValueFromElement(ELEMENT_PRICE_CURENCY, ELEMENT_PRICE_CURENCY, ElementValue.TEXT);
    }

    public boolean validateAnunt() {
        return validatePret(CONSIDER_ONLY_APARTMENTS_WITH_PRICE_LARGER_THEN, CONSIDER_ONLY_APARTMENTS_WITH_PRICE_SMALLER_THEN, PageType.APARTAMENT)
                && validateMetriPatrati(CONSIDER_ONLY_APARTMENTS_WITH_MP_LARGER_THEN, CONSIDER_ONLY_APARTMENTS_WITH_MP_SMALLER_THEN, PageType.APARTAMENT)
                && validatePriceCurrency(PageType.APARTAMENT) && validatePretPeMetruPatrat(PageType.APARTAMENT);
    }

    @Override
    public BigDecimal getPretPeMetruPatratFromElement() {
        return Calculations.calculatePretPeMetruPatrat(pret, metriPatrati);
    }
}
