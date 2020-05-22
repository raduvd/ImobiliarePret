package imobiliare.anunt;

import imobiliare.Calculations;
import imobiliare.enums.ElementValue;
import imobiliare.enums.PageType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class AnuntCasa extends Anunt {

    public static final By ELEMENT_METRI_PATRATI_UTILI = By.xpath(".//ul[@class= 'caracteristici']/li[2]");
    public static final By ELEMENT_METRI_PATRATI_UTILI_FALLBACK = By.xpath(".//ul[@class= 'caracteristici']/li[1]");

    private double CONSIDER_ONLY_HOUSES_WITH_PRICE_LARGER_THEN = 10000;
    private double CONSIDER_ONLY_HOUSES_WITH_PRICE_SMALLER_THEN = 800000;
    private double CONSIDER_ONLY_HOUSES_WITH_MP_LOCUIBILI_LARGER_THEN = 20;
    private double CONSIDER_ONLY_HOUSES_WITH_MP_LOCUIBILI_SMALLER_THEN = 450;

    public AnuntCasa(WebElement elementulAnunt, PageType pageType) {
        super(elementulAnunt, pageType);
        this.pret = getPretFromElement();
        this.metriPatrati = getMetripatratiFromElement();
        this.pretPeMetruPatrat = getPretPeMetruPatratFromElement();
    }

    /**
     * Calculul il fac aici nu prea corect, calculez pret/metruPatratUtil si IGNOR restul de teren al casei.
     * Oricum eu vreau sa imi fac doar o idee despre preturi.
     *
     * @return
     */
    public BigDecimal getMetripatratiFromElement() {
        return (BigDecimal) getValueFromElement(ELEMENT_METRI_PATRATI_UTILI, ELEMENT_METRI_PATRATI_UTILI_FALLBACK, ElementValue.METRI_PATRATI);
    }

    @Override
    public boolean validateAnunt() {
        return validatePret(CONSIDER_ONLY_HOUSES_WITH_PRICE_LARGER_THEN, CONSIDER_ONLY_HOUSES_WITH_PRICE_SMALLER_THEN, PageType.CASE)
                && validateMetriPatrati(CONSIDER_ONLY_HOUSES_WITH_MP_LOCUIBILI_LARGER_THEN, CONSIDER_ONLY_HOUSES_WITH_MP_LOCUIBILI_SMALLER_THEN, PageType.CASE)
                && validatePriceCurrency(PageType.CASE) && validatePretPeMetruPatrat(PageType.CASE);
    }

    @Override
    public BigDecimal getPretPeMetruPatratFromElement() {
        return Calculations.calculatePretPeMetruPatrat(pret, metriPatrati);
    }
}
