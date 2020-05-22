package imobiliare;

import imobiliare.anunt.Anunt;
import imobiliare.enums.PageType;
import imobiliare.result.Result;
import imobiliare.webDriver.WebDriverImobiliare;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainTest {

    //Aici ar trebui sa mearga pana la maxim tot timpul, deci pune cam 500
    public static final int NUMBER_OF_PAGES = 500;

    @Test
    public void testAppartamente() {
        getAverageFor(PageType.APARTAMENT);
    }

    @Test
    public void testTeren() {
        getAverageFor(PageType.TEREN);
    }

    @Test
    public void testCase() {
        getAverageFor(PageType.CASE);
    }

    private void getAverageFor(PageType pageType) {
        Page page = new Page(pageType);
        //Only unique Anunt will be added
        Set<Anunt> hashSet = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            hashSet.addAll(page.getListaDeAnunturiDinPage());
            //if the nextPage() returns false (the page was not changed) finish the loop with
            if (!page.nextPage()) {
                break;
            }
        }
        String average = Calculations.calculateAverage(hashSet).toString();
        WriteToExcel.writeToExcel(new ArrayDeque<Object>(Arrays.asList(average, hashSet.size())), pageType);
    }

    @After
    public void cleanup() {
        WebDriverImobiliare.closeWebDriver();
    }

    @AfterClass
    public static void sumUp() {
        Result.sumUpPrint();
    }
}
