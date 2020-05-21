package imobiliare;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Data
@EqualsAndHashCode
public class Anunt {

    public static final By ELEMENT_CARACTERISTICI = By.xpath(".//ul[@class= 'caracteristici']");
    public static final By ELEMENT_METRI_PATRATI = By.xpath(".//ul[@class= 'caracteristici']/li[2]");
    public static final By ELEMENT_PRET = By.xpath(".//div[@class= 'pret']/span");
    public static final By ELEMENT_PRET_WITH_EXTRA_DIV = By.xpath(".//div[@class= 'pret']/div/span");
    public static final By ELEMENT_PRICE_CURENCY = By.xpath(".//span[@itemprop= 'priceCurrency']");


    private WebElement elementulAnunt;
    private Double pret;
    private Double metriPatrati;
    private String priceCurrency;

    public Anunt(WebElement elementulAnunt) {
        this.elementulAnunt = elementulAnunt;
        this.pret = getPretFromElement(elementulAnunt);
        this.metriPatrati = getMetripatratiFromElement(elementulAnunt);
        this.priceCurrency = getPriceCurrencyFromElement(elementulAnunt);
    }

    private Double getPretFromElement(WebElement elementulAnunt) {
        WebElement pretElement;

        try {
            pretElement = elementulAnunt.findElement(ELEMENT_PRET);
        } catch (Exception e) {
            pretElement = elementulAnunt.findElement(ELEMENT_PRET_WITH_EXTRA_DIV);
        }
        checkIfElementIsNull(pretElement, "pretElement", ELEMENT_PRET.toString() + " sau " + ELEMENT_PRET_WITH_EXTRA_DIV.toString());

        String stringValue = pretElement.getText();
        if (stringValue == null)
            return null;

        stringValue = stringValue.replace(".", "");

        Double doubleValue;
        try {
            doubleValue = Double.parseDouble(stringValue);
        } catch (Exception e) {
            throw new RuntimeException("Pretul nu a putut fi transformat in double, " +
                    "cea ce inseamna ca nu e numar cea ce am gasit");
        }
        return doubleValue;
    }

    private Double getMetripatratiFromElement(WebElement elementulAnunt) {
        final WebElement metriPatrati = elementulAnunt.findElement(ELEMENT_METRI_PATRATI);

        checkIfElementIsNull(metriPatrati, "metriPatrati", ELEMENT_METRI_PATRATI.toString());
        String stringValue = metriPatrati.getText();

        if (stringValue == null)
            return null;

        stringValue = stringValue.split(" ")[0];
        Double doubleValue;
        try {
            doubleValue = Double.parseDouble(stringValue);
        } catch (Exception e) {
            throw new RuntimeException("Pretul nu a putut fi transformat in double, " +
                    "cea ce inseamna ca nu e numar cea ce am gasit");
        }
        return doubleValue;
    }


    private String getPriceCurrencyFromElement(WebElement elementulAnunt) {
        final WebElement priceCurrency = elementulAnunt.findElement(ELEMENT_PRICE_CURENCY);
        checkIfElementIsNull(priceCurrency, "priceCurrency", ELEMENT_PRICE_CURENCY.toString());

        String stringValue = priceCurrency.getText();

        if (stringValue == null)
            return null;

        stringValue = stringValue.split(" ")[0];

        return stringValue;
    }

    private void checkIfElementIsNull(Object element, String elementName, String xpathUsed) {
        if (element == null)
            throw new RuntimeException("Nu a fost gasit un element div care sa reprezinte " +
                    elementName + ", folosind acest XPATH_PRET: " + xpathUsed);
    }

    //LOGICA DE BLOC NOU care e predispusa la fail astfel am scos-o
    //public static final By ELEMENT_VECHIME = By.xpath(".//ul[@class= 'caracteristici']/li[5]");
    //private Boolean isBlocNou;
    //in constructor --> this.isBlocNou = getIsBlocNouFromElement(elementulAnunt);
    /*private Boolean getIsBlocNouFromElement(WebElement elementulAnunt) {
        final WebElement vechime = elementulAnunt.findElement(ELEMENT_VECHIME);
        checkIfElementIsNull(vechime, "vechime", ELEMENT_VECHIME.toString());

        final String stringValue = vechime.getText();
        System.out.println(stringValue);
        return stringValue.equals("Bloc Nou");
    }*/

    public String toString() {
        return "Anunt: " + pret + " " + priceCurrency + ", " + metriPatrati + " MP";
    }
}
