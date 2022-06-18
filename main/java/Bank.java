package BankOffers;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Bank {

    String DRIVER_NAME;
    String DRIVER_PATH;
    String LOGIN_URL;
    String USERNAME_FIELD;
    String PASSWORD_FIELD;
    String LOGIN_BUTTON;
    String POST_LOGIN_CHECK;
    String OFFERS_URL;
    String OFFERS_PAGE_CHECK;
    String ADD_DEAL_ELEMENT;
    String VENDOR_NAME_ELEMENT;
    String VENDOR_NAME_ATTRIBUTE;
    String VALUE_ELEMENT;
    String VALUE_ATTRIBUTE;
    String EXPIRATION_ELEMENT;
    String EXPIRATION_ATTRIBUTE;

    public Bank(String s) {

        DRIVER_NAME = "webdriver.chrome.driver";
        DRIVER_PATH = "C:\\Users\\kevin\\OneDrive\\Documents\\Programming\\ProjectsCreditCardRewards\\main\\resources\\chromedriver_win32\\chromedriver.exe";

        if (s == "Bank of America") {
            LOGIN_URL = "https://www.bankofamerica.com";
            USERNAME_FIELD = "//*[@id=\"onlineId1\"]";
            PASSWORD_FIELD = "//*[@id=\"passcode1\"]";
            LOGIN_BUTTON = "//*[@id=\"signIn\"]";
            POST_LOGIN_CHECK = "//*[@id=\"olb-globals-header-container\"]/*//*/*/*/a[@name=\"onh_sign_off\"]";
            OFFERS_URL = "https://secure.bankofamerica.com/customer-deals/";
            OFFERS_PAGE_CHECK = "//*[@id=\"skip-to-h1\"]";
            ADD_DEAL_ELEMENT = "//*/div/div/div[1]/div[4]/div[1]/a";
            VENDOR_NAME_ELEMENT = "//*[@id=\"company_product_desc_ada\"]/img";
            VENDOR_NAME_ATTRIBUTE = "alt";
            VALUE_ELEMENT = "//*[@id=\"bamdAvailableDeals\"]/div[7]/div/div[1]/div[4]/div[1]/span";
            VALUE_ATTRIBUTE = "innerHTML";
            EXPIRATION_ELEMENT = "//*[@id=\"bamdAvailableDeals\"]/div[7]/div/div[1]/div[4]/div[2]/p[1]";
            EXPIRATION_ATTRIBUTE = "innerHTML";
            return;
        }

        if (s == "American Express") {
            LOGIN_URL = "https://www.americanexpress.com/en-us/account/login";
            USERNAME_FIELD = "//*[@id=\"eliloUserID\"]";
            PASSWORD_FIELD = "//*[@id=\"eliloPassword\"]";
            LOGIN_BUTTON = "//*[@id=\"loginSubmit\"]";
            POST_LOGIN_CHECK = "//*[@id=\"gnav_login\"]";
            OFFERS_URL = "https://global.americanexpress.com/offers/eligible?filter=cash";
            OFFERS_PAGE_CHECK = "//*[@id=\"offers\"]/div/section[1]/span";
            ADD_DEAL_ELEMENT = "//*/div/div/div[4]/div/button[@title=\"Add to Card\"]";
            VENDOR_NAME_ELEMENT = "//*/div/div/div[2]/p[2]";
            VENDOR_NAME_ATTRIBUTE = "innerHTML";
            VALUE_ELEMENT = "//*/div/div/div[2]/p[1][@class=\"heading-3 margin-0-b dls-accent-gray-06\"]";
            VALUE_ATTRIBUTE = "innerHTML";
            EXPIRATION_ELEMENT = "//*/div/div/div[3][@class=\"offer-expires offer-column margin-auto-l margin-b-md-down col-md-2\"]";
            EXPIRATION_ATTRIBUTE = "innerHTML";
            return;
        }

    }

    public int scrape() throws InterruptedException, ParseException, IOException {
        // Returns the number of new items added

        // Initialize all drivers
        Console console = System.console();
        System.setProperty(DRIVER_NAME, DRIVER_PATH);
        // configure options parameter to Chrome driver
        ChromeOptions o = new ChromeOptions();
        // add Incognito parameter
        o.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(o);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login and navigate to deals
        driver.manage().window().maximize();
        driver.get(LOGIN_URL);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(USERNAME_FIELD)));
        WebElement usernameElement = driver.findElement(By.xpath(USERNAME_FIELD));
        WebElement passwordElement = driver.findElement(By.xpath(PASSWORD_FIELD));
        WebElement login = driver.findElement(By.xpath(LOGIN_BUTTON));
        String usernameInput = new String(console.readLine("Username:"));
        usernameElement.sendKeys(usernameInput);
        String passwordInput = new String(console.readPassword("Password:"));
        passwordElement.sendKeys(passwordInput);
        login.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(POST_LOGIN_CHECK))); // Test for wrong page
        Thread.sleep(1000);
        driver.get(OFFERS_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OFFERS_PAGE_CHECK)));
        Thread.sleep(5000);

        // Create list of buttons to add deal. Deals previously added not included.
        List<WebElement> addDeals = driver.findElements(By.xpath(ADD_DEAL_ELEMENT));
        for (WebElement deal : addDeals) {
            System.out.println("Add deal link:" + deal.getAttribute("href") + " ");
            // addDeals.click();
        }

        // Create list of elements representing each deal
        List<WebElement> vendorElements = driver.findElements(By.xpath(VENDOR_NAME_ELEMENT));
        List<WebElement> valueElements = driver.findElements(By.xpath(VALUE_ELEMENT));
        List<WebElement> expirationElements = driver.findElements(By.xpath(EXPIRATION_ELEMENT));

        System.out.println(vendorElements.size());
        for (int i = 0; i < vendorElements.size(); i++) {

            // Vendor name
            String vendor = vendorElements.get(i).getAttribute(VENDOR_NAME_ATTRIBUTE);

            // Value of deal as percent or $ amount
            String value = valueElements.get(i).getAttribute(VALUE_ATTRIBUTE);
            // Double percent = 0.0;
            // Double amount = 0.0;
            // Double minimum;
            // Double maximum;
            // if (value.contains("%")) {
            // percent = Double.parseDouble(value.replace("%", "")) / 100;
            // }
            // if (value.contains("$")) {
            // amount = Double.parseDouble(value.replace("$", ""));
            // }

            // Expiration date
            String expirationText = expirationElements.get(i).getAttribute(EXPIRATION_ATTRIBUTE).replace("Exp.", "");
            // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            // Date expirationDate = formatter.parse(expirationText);

            System.out.println("Vendor=" + vendor + " Value=" + value + " " + " Exp="
                    + expirationText);

            // Create deal? or insert to database?
        }
        // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
        return 0;
    }

}
