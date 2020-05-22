package imobiliare.webDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverImobiliare {
    /**
     * Aici am pathul local (de pe PC) catre chromedriver.exe
     */
    private static final String CHROMEDRIVER_EXECUTABLE = "C:\\Users\\vancer\\Desktop\\Libraryes & Sources\\chromedriver_win32\\chromedriver.exe";
    private static WebDriver webDriver;

    private WebDriverImobiliare() {
    }

    public static WebDriver getWebDriver() {
        if (webDriver == null) {
            System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_EXECUTABLE);
            webDriver = new ChromeDriver();
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    public static void closeWebDriver() {
        webDriver.close();
        webDriver = null;
    }
}


