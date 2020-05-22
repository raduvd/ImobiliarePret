package imobiliare;

import imobiliare.anunt.Anunt;
import imobiliare.anunt.AnuntApartament;
import imobiliare.anunt.AnuntCasa;
import imobiliare.anunt.AnuntTeren;
import imobiliare.enums.PageType;
import imobiliare.result.Result;
import imobiliare.result.ErrorType;
import imobiliare.webDriver.WaitDriverImobiliare;
import imobiliare.webDriver.WebDriverImobiliare;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static imobiliare.result.Result.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class Page {

    public static final By ELEMENT_ACCEPT_ALL_COOKIES = By.xpath("//div[@aria-hidden='false']/div/div/div/div[2]/div[2]/div[2]/a");
    public static final By ELEMENT_ANUNT = By.xpath("//div[starts-with(@id, 'anunt-')]");
    public static final By ELEMENT_PARENT_PAGINARE = By.className("index_paginare");
    public static final By ELEMENT_ACTIVE_PAGE_NUMBER = By.xpath("//a[@class ='active']");

    private WebDriver webDriver;
    private Integer pageNumber;
    private PageType pageType;

    public Page(PageType pageType) {
        this.pageType = pageType;
        this.webDriver = WebDriverImobiliare.getWebDriver();
        webDriver.get(pageType.getLinkToPage());
        waitForPageCookiesAndAcceptIt();
        this.pageNumber = waitForActivePageNumberAndReturnThePageNumber();
        if (pageNumber == null)
            throw new RuntimeException("CANNOT GET PAST THE FIRST PAGE");
        System.out.println("Page number is: " + pageNumber);
    }

    /**
     * This is one of the last element on the page, so when the page is active, we can safely say the page is loaded.
     */
    private Integer waitForActivePageNumberAndReturnThePageNumber() {
        int secondsToWait = 25;
        final WebElement pageNumberElement;
        try {
            //asteptam pana cand elementul apare pe pagina si are o valoare in el
            WaitDriverImobiliare.
                    getWaitDriver(secondsToWait).
                    until((ExpectedCondition<Boolean>) webDriver -> {
                        final String text = webDriver.findElement(ELEMENT_ACTIVE_PAGE_NUMBER).getText();
                        return text != null && !text.isEmpty();
                    });
            pageNumberElement = webDriver.findElement(ELEMENT_ACTIVE_PAGE_NUMBER);
        } catch (Exception e) {
            Result.add(ErrorType.WAITING_TIMEOUT, ELEMENT_ACTIVE_PAGE_NUMBER.toString(), null, e.getMessage(), e, PageType.GENERAL);
            return null;
        }

        final String activePage = pageNumberElement.getText();
        try {
            final Integer integer = Integer.valueOf(activePage);
            return integer;
        } catch (Exception e) {
            Result.add(ErrorType.CASTING_EXCEPTION, ELEMENT_ACTIVE_PAGE_NUMBER.toString(), null, e.getMessage(), e, PageType.GENERAL);
            return null;
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
            Result.add(ErrorType.WAITING_TIMEOUT, ELEMENT_ACCEPT_ALL_COOKIES.toString(), null, e.getMessage(), e, PageType.GENERAL);
        }
    }

    public List<Anunt> getListaDeAnunturiDinPage() {
        List<Anunt> anuntLista = new ArrayList<>();

        for (WebElement webElement : webDriver.findElements(ELEMENT_ANUNT)) {
            Anunt anunt;
            switch (pageType) {
                case APARTAMENT:
                    anunt = new AnuntApartament(webElement, pageType);
                    break;
                case TEREN:
                    anunt = new AnuntTeren(webElement, pageType);
                    break;
                case CASE:
                    anunt = new AnuntCasa(webElement, pageType);
                    break;
                default:
                    anunt = null;
            }
            if (!anunt.validateAnunt())
                continue;
            anuntLista.add(anunt);
        }
        return anuntLista;
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
            Result.add(ErrorType.ELEMENT_NOT_FOUND, "icon-portal-arrow-right", null, e.getMessage(), e, PageType.GENERAL);
            return false;
        }

        //wait the next page to pe active
        final Integer activePage = waitForActivePageNumberAndReturnThePageNumber();
        if (activePage == null || activePage == this.pageNumber) {
            return false;
        } else {
            System.out.println("New Page number is: " + activePage);
            this.pageNumber = activePage;
            NUMARUL_PAGINII++;
            return true;
        }
    }
}
