package bankoffers;

import java.util.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BankScraper {
    String BANK_NAME;
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

    public static enum BankType {
        BANK_OF_AMERICA,
        AMERICAN_EXPRESS,
        CITIBANK,
        CAPITAL_ONE
    }

    public static BankScraper forBankOfAmerica() {
        return new BankScraper(BankType.BANK_OF_AMERICA);
    }

    public static BankScraper forAmericanExpress() {
        return new BankScraper(BankType.AMERICAN_EXPRESS);
    }

    public static BankScraper forCitibank() {
        return new BankScraper(BankType.CITIBANK);
    }

    public static BankScraper forCapitalOne() {
        return new BankScraper(BankType.CAPITAL_ONE);
    }

    private BankScraper(BankType bank) {

        DRIVER_NAME = "webdriver.chrome.driver";
        DRIVER_PATH = "CreditCardRewards\\main\\resources\\chromedriver_win32\\chromedriver.exe";

        switch (bank) {
            case BANK_OF_AMERICA:
                BANK_NAME = "Bank of America";
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
                break;
            case AMERICAN_EXPRESS:
                BANK_NAME = "American Express";
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
                VALUE_ELEMENT = "//*/div/div/div[@class=\"offer-info offer-column margin-b-md-down col-md-5\"]/p[1]";
                VALUE_ATTRIBUTE = "innerHTML";
                EXPIRATION_ELEMENT = "//*/div/div/div[3][@class=\"offer-expires offer-column margin-auto-l margin-b-md-down col-md-2\"]/span";
                EXPIRATION_ATTRIBUTE = "innerHTML";
                break;
            case CITIBANK: // incomplete
                BANK_NAME = "Citibank";
                LOGIN_URL = "https://www.citi.com/";
                USERNAME_FIELD = "//*[@id=\"username\"]";
                PASSWORD_FIELD = "//*[@id=\"password\"]";
                LOGIN_BUTTON = "//*[@id=\"signInBtn\"]";
                POST_LOGIN_CHECK = "";
                OFFERS_URL = "https://online.citi.com/US/ag/merchantoffers";
                OFFERS_PAGE_CHECK = "//*[@id=\"maincontent\"]/div/div/div/app-merchantoffers/citi-simple-layout/citi-row[1]/div/citi-column/div/citi-text-header/h1";
                ADD_DEAL_ELEMENT = "//*[@id=\"3f531a21-679b-a47b-e3f3-79ecc5a54f99\"]"; // There
                VENDOR_NAME_ELEMENT = "//*[@id=\"loading\"]/citi-row[5]/div/citi-column/div/citi-row/div/div/div[4]/div[1]";
                VENDOR_NAME_ATTRIBUTE = "innerHTML";
                VALUE_ELEMENT = "";
                VALUE_ATTRIBUTE = "innerHTML";
                EXPIRATION_ELEMENT = "";
                EXPIRATION_ATTRIBUTE = "innerHTML";
                break;
            case CAPITAL_ONE:
                // whatever
                break;
            default:
                throw new IllegalArgumentException("idk");

        }

    }

    public int scrape() throws InterruptedException, ParseException, IOException, SQLException {
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
        DBReadWrite dbrw = new DBReadWrite();

        for (int i = 0; i < vendorElements.size(); i++) {

            // Vendor name
            String vendor = vendorElements.get(i).getAttribute(VENDOR_NAME_ATTRIBUTE);

            // Value of deal as percent or $ amount
            String value = valueElements.get(i).getAttribute(VALUE_ATTRIBUTE);

            // Expiration date
            String expirationText = expirationElements.get(i).getAttribute(EXPIRATION_ATTRIBUTE).replace("Exp.", "");
            // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            // Date expirationDate = formatter.parse(expirationText);

            // Create Offer object
            String vendorName;
            double[] valueList;
            Date expDate;
            switch (BANK_NAME) {
                case "Bank of America":
                    vendorName = parseBOAVendor(vendor);
                    valueList = parseBOAValue(value);
                    expDate = parseBOAExpiration(expirationText);
                    break;
                case "American Express":
                    vendorName = parseAMEXVendor(vendor);
                    valueList = parseAMEXValue(value);
                    expDate = parseAMEXExpiration(expirationText);
                    break;
                case "Citibank":
                    vendorName = "";
                    valueList = new double[] { 0, 0, 0, 0 };
                    expDate = new Date();
                    break;
                case "Capital One":
                    vendorName = "";
                    valueList = new double[] { 0, 0, 0, 0 };
                    expDate = new Date();
                    break;
                default:
                    vendorName = "";
                    valueList = new double[] { 0, 0, 0, 0 };
                    expDate = new Date();
            }

            Offer nextOffer = new Offer(BANK_NAME, vendorName, valueList[0], valueList[1], valueList[2], valueList[3],
                    expDate);
            System.out.println(nextOffer.toString());
            dbrw.write(nextOffer);
        }
        // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
        dbrw.close();
        return vendorElements.size();
    }

    // BOA Parsers
    private static String parseBOAVendor(String vendor) {
        return vendor.substring(0, vendor.length() - 6);
    }

    private static double[] parseBOAValue(String value) {
        double percent = 0.0;
        double amount = 0.0;
        double minimum = 0.0;
        double maximum = 99999.9;
        if (value.contains("%")) {
            percent = Double.parseDouble(value.replace("%", "")) / 100;
        }
        if (value.contains("$")) {
            amount = Double.parseDouble(value.replace("$", ""));
        }
        return new double[] { percent, amount, minimum, maximum };
    }

    private static Date parseBOAExpiration(String expirationText) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        return formatter.parse(expirationText);
    }

    // AMEX Parsers
    private static String parseAMEXVendor(String vendor) {
        return vendor;
    }

    private static double[] parseAMEXValue(String value) {
        // Examples:
        // Get 2% back on purchases, up to a total of $250
        // Spend $125 or more, get $25 back. Up to 2 times
        // Spend $15 or more, get 20% back, up to a total of $40
        // Get 50% back for up to 3 months, up to a total of $55. Enroll by 10/1/2022.
        // Spend $75+, get $5 back, up to 2X times (total of $10) after you Dine Small
        double percent = 0.0;
        double amount = 0.0;
        double minimum = 0.0;
        double maximum = 99999.9;

        // Find deal value as percent or amount
        String dealValue = value.substring(value.toLowerCase().indexOf("get") + 4,
                value.toLowerCase().indexOf("back") - 1);
        if (dealValue.contains("%")) {
            percent = Double.parseDouble(dealValue.replaceAll("[^0-9.]+", "")) / 100;
        } else {
            amount = Double.parseDouble(dealValue.replaceAll("[^0-9.]+", ""));
        }
        // Find minimum
        if (value.contains("or more")) {
            String minRequire = value.substring(value.toLowerCase().indexOf("spend") + 6,
                    value.toLowerCase().indexOf("or more") - 1);
            minimum = Double.parseDouble(minRequire.replaceAll("[^0-9.]+", ""));
        }
        // Find maximum
        if (value.contains("up to a total of")) {
            String tempMax = "";
            String maxReward = value.substring(value.toLowerCase().indexOf("up to a total of") + 17);
            if (maxReward.contains(" ")) { // There are words after the $ value
                tempMax = maxReward.substring(0, maxReward.indexOf(" ") - 1);
            } else {
                tempMax = maxReward;
            }
            maximum = Double.parseDouble(tempMax.replaceAll("[^0-9.]+", ""));
        }

        return new double[] { percent, amount, minimum, maximum };
    }

    private static Date parseAMEXExpiration(String expirationText) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        if (expirationText.contains("Expires")) {
            int days;
            if (expirationText.contains("tomorrow")) {
                days = 1;
            } else {
                days = Integer.parseInt(expirationText.replaceAll("[^\\d.]", ""));
            }
            Date date = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);
            return cal.getTime();
        } else {

            return formatter.parse(expirationText);
        }
    }

    // Citi Parsers

    // Chase Parsers
}
