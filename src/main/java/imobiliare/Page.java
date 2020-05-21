package imobiliare;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

@Data
public class Page {

    private double CONSIDER_ONLY_APARTMENTS_WITH_PRICE_LARGER_THEN = 20000;
    private double CONSIDER_ONLY_APARTMENTS_WITH_PRICE_SMALLER_THEN = 190000;
    private double CONSIDER_ONLY_APARTMENTS_WITH_MP_LARGER_THEN = 20;
    private double CONSIDER_ONLY_APARTMENTS_WITH_MP_SMALLER_THEN = 150;

    public static final By ELEMENT_ACCEPT_ALL_COOKIES = By.xpath("//div[@aria-hidden='false']/div/div/div/div[2]/div[2]/div[2]/a");
    public static final By ELEMENT_ANUNT = By.xpath("//div[starts-with(@id, 'anunt-')]");
    public static final By ELEMENT_PARENT_PAGINARE = By.className("index_paginare");
    public static final By ELEMENT_ACTIVE_PAGE_NUMBER = By.xpath("//a[@class ='active']");

    private WebDriver webDriver;
    private int pageNumber;

    public Page(String linkToPage) {
        this.webDriver = WebDriverImobiliare.getWebDriver();
        webDriver.get(linkToPage);
        waitForPageCookiesAndAcceptIt();
        this.pageNumber = waitForActivePageNumberAndReturnThePageNumber();
    }

    /**
     * This is one of the last element on the page, so when the page is active, we can safely say the page is loaded.
     */
    private int waitForActivePageNumberAndReturnThePageNumber() {
        int secondsToWait = 15;
        try {
            //asteptam pana cand elementul apare pe pagina si are o valoare in el
            WaitDriverImobiliare.
                    getWaitDriver(secondsToWait).
                    until((ExpectedCondition<Boolean>) webDriver -> {
                        final String text = webDriver.findElement(ELEMENT_ACTIVE_PAGE_NUMBER).getText();
                        return text != null && !text.isEmpty();
                    });

            final WebElement pageNumberElement = webDriver.findElement(ELEMENT_ACTIVE_PAGE_NUMBER);
            final String activePage = pageNumberElement.getText();
            return Integer.valueOf(activePage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("The active page is not a number " +
                    "or is null " +
                    "or no active page number was found using xpath: " + ELEMENT_ACTIVE_PAGE_NUMBER);
        }
    }

    private void waitForPageCookiesAndAcceptIt() {
        int sectondsToWait = 15;
        try {
            WaitDriverImobiliare.
                    getWaitDriver(sectondsToWait).
                    until(ExpectedConditions.presenceOfElementLocated(ELEMENT_ACCEPT_ALL_COOKIES));
            webDriver.findElement(ELEMENT_ACCEPT_ALL_COOKIES).click();
        } catch (Exception e) {
            System.out.println("The Accept Cookies element did not appear on the screen after waiting: " + sectondsToWait);
        }
    }

    public List<Anunt> getListaDeAnunturiDinPage() {
        List<Anunt> anuntLista = new ArrayList<>();

        for (WebElement webElement : webDriver.findElements(ELEMENT_ANUNT)) {
            Anunt anunt = new Anunt(webElement);
            if (validateAnunt(anunt))
                continue;
            anuntLista.add(anunt);
        }
        return anuntLista;
    }

    /**
     * Validarile le fac si pentru a ma asigura ca datele ce le am sunt valide.
     * Spre exemplu un metruPatrat cu valoarea de 123000 nu e un metru patrat, nu e data valide.
     * <p>
     * Totodata fac aceste validari pentru a nu mi se strica calculele.
     * Spre exemplu un penthouse cu pretul de 1milion de dolari imi strica calculele de medie, astfel mai bine il scot.
     */
    private boolean validateAnunt(Anunt anunt) {
        final Double pret = anunt.getPret();
        if (pret == null
                || pret < CONSIDER_ONLY_APARTMENTS_WITH_PRICE_LARGER_THEN
                || pret > CONSIDER_ONLY_APARTMENTS_WITH_PRICE_SMALLER_THEN) {

            System.out.println("Anuntul a fost eliminat din calcul deoarece am un pret non-valid: " + anunt.getPret());
            return false;
        }

        final Double metriPatrati = anunt.getMetriPatrati();
        if (metriPatrati == null
                || metriPatrati < CONSIDER_ONLY_APARTMENTS_WITH_MP_LARGER_THEN
                || metriPatrati > CONSIDER_ONLY_APARTMENTS_WITH_MP_SMALLER_THEN) {
            System.out.println("Anuntul a fost eliminat din calcul deoarece am un metruPatrat non-valid: " + anunt.getMetriPatrati());
            return false;
        }

        final String currency = anunt.getPriceCurrency();
        if (currency == null || !currency.equals("EUR")) {
            System.out.println("Anuntul a fost eliminat din calcul deoarece am un Currency non-valid: " + anunt.getPriceCurrency());
            return false;
        }
        return true;
    }

    /**
     * @return the success or failure of going to the next page
     */
    public boolean nextPage() {
        //Click on the next button
        try {
            webDriver.findElement(ELEMENT_PARENT_PAGINARE).
                    findElement(By.className("icon-portal-arrow-right")).click();
        } catch (Exception e) {
            System.out.println("The arrow button cannot be fond, is it because " +
                    "there were no more pages? Or xpath is wrong?");
            return false;
        }

        //wait the next page to pe active
        final int activePage = waitForActivePageNumberAndReturnThePageNumber();
        if (activePage == this.pageNumber) {
            return false;
        } else {
            System.out.println("New Page number is: " + activePage);
            this.pageNumber = activePage;
            return true;
        }
    }
}
