package imobiliare;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverImobiliare {

    private static final String CHROMEDRIVER_EXECUTABLE = "C:\\Users\\vancer\\Desktop\\Libraryes & Sources\\chromedriver_win32\\chromedriver.exe";
    private static WebDriver webDriver;

    private WebDriverImobiliare() {
    }

    //singleton
    public static WebDriver getWebDriver() {
        if (webDriver == null) {
            System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_EXECUTABLE);
            webDriver = new ChromeDriver();
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }
}


