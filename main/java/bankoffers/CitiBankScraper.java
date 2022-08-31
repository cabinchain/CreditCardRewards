package bankoffers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CitiBankScraper implements BankScraper {
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
    String VALUE_ELEMENT_2;
    String VALUE_ATTRIBUTE_2;
    String EXPIRATION_ELEMENT;
    String EXPIRATION_ATTRIBUTE;

    public CitiBankScraper() {

        DRIVER_NAME = "webdriver.chrome.driver";
        DRIVER_PATH = "CreditCardRewards\\main\\resources\\chromedriver_win32\\chromedriver.exe";
        BANK_NAME = "Citibank";
        LOGIN_URL = "https://www.citi.com/";
        USERNAME_FIELD = "//*[@id=\"username\"]";
        PASSWORD_FIELD = "//*[@id=\"password\"]";
        LOGIN_BUTTON = "//*[@id=\"signInBtn\"]";
        POST_LOGIN_CHECK = "//*[@id=\"welcomeBarMessage\"]";
        OFFERS_URL = "https://online.citi.com/US/ag/merchantoffers";
        OFFERS_PAGE_CHECK = "//*[@id=\"maincontent\"]/div/div/div/app-merchantoffers/citi-simple-layout/citi-row[1]/div/citi-column/div/citi-text-header/h1";

        ADD_DEAL_ELEMENT = "//*[@id=\"3f531a21-679b-a47b-e3f3-79ecc5a54f99\"]";

        VENDOR_NAME_ELEMENT = "//*[@class=\"mo-modal-merchant-name\"]";
        VENDOR_NAME_ATTRIBUTE = "innerHTML";
        VALUE_ELEMENT = "//*[@class=\"small ng-star-inserted\"][2]";
        VALUE_ATTRIBUTE = "innerHTML";
        VALUE_ELEMENT_2 = "//*[@class=\"mo-modal-offer-title ng-star-inserted\"]";
        VALUE_ATTRIBUTE_2 = "innerHTML";
        EXPIRATION_ELEMENT = "//*[@class=\"mo-modal-header-date ng-star-inserted\"]/span[2]";
        EXPIRATION_ATTRIBUTE = "innerHTML";

    }

    public List<Offer> scrape() throws InterruptedException, ParseException, IOException, SQLException {
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

        List<Offer> newOfferList = new ArrayList<Offer>();

        // Enter each Section by clicking the "Show More Offers" Button
        List<WebElement> dealSections = driver.findElements(By.xpath("//*[@value=\"Show More Offers\"]/button"));
        int cat = 0; // category number used to denote which section a deal is in
        for (WebElement section : dealSections) {
            section.click();
            List<WebElement> dealCards = driver.findElements(
                    By.xpath("//*[@class=\"col-xs-12 col-md-3 offer cat-" + cat + " ng-star-inserted\"]"));
            for (WebElement card : dealCards) {
                // Click on See Details within the dealCard
                card.findElement(By.xpath("//*[@class=\"btn btn-primary hero-button ng-star-inserted\"]")).click();
                // Press button to add offer
                card.findElement(By.xpath("(//*[@role=\"dialog\"])[3]")).click();
                // Read card and create new Offer
                String vendor = driver.findElement(By.xpath(VENDOR_NAME_ELEMENT)).getAttribute(VENDOR_NAME_ATTRIBUTE);
                String value = driver.findElement(By.xpath(VALUE_ELEMENT)).getAttribute(VALUE_ATTRIBUTE);
                String backupValue = driver.findElement(By.xpath(VALUE_ELEMENT)).getAttribute(VALUE_ATTRIBUTE);
                String expirationString = driver.findElement(By.xpath(EXPIRATION_ELEMENT))
                        .getAttribute(EXPIRATION_ATTRIBUTE);
                Optional<Offer> newOffer = createOffer(vendor, value, backupValue, expirationString);
                newOffer.ifPresent(offer -> newOfferList.add(offer));
            }

        }

        return newOfferList;
        // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
    }

    private Optional<Offer> createOffer(String vendor, String value, String backupValue, String expiration)
            throws ParseException {
        // If offer parameters are not parsable, we return null
        Offer createdOffer = null;
        try {
            // Create Offer object
            String vendorName;
            OfferSavingsValues savingsValues;
            LocalDate expDate;
            vendorName = parseCitiVendor(vendor);
            try {
                savingsValues = parseCitiValue(value);
            } catch (ParseException e) {
                savingsValues = parseCitiBackupValue(backupValue);
            }

            expDate = parseCitiExpiration(expiration);

            createdOffer = new Offer(BANK_NAME, vendorName, savingsValues.getPercent(),
                    savingsValues.getAmount(), savingsValues.getMinimum(), savingsValues.getMaximum(), expDate);
            System.out.println(createdOffer.toString());

        } catch (NumberFormatException | ParseException e) { // Should I not catch an unchecked exception?
            System.out.println("Values not parseable, offer skipped: " + e);
        }
        return Optional.ofNullable(createdOffer);
    }

    // Citibank parsers
    static String parseCitiVendor(String vendor) {
        return vendor;
    }

    static OfferSavingsValues parseCitiValue(String value) throws ParseException {
        // Examples:
        // Earn $9.99 back on a purchase of $50 or more.
        // Earn $10 back on any purchase.
        // Earn $10 back on your next purchase of $20 or more at bp and Amoco when you
        // use the BPme app!
        // Earn 20% back on any purchase.
        // Earn 10% back on a purchase of $100 or more.
        // Spend $50 with LugLess on your first luggage shipment and get a $20 statement
        // credit.

        double percent = 0.0;
        double amount = 0.0;
        double minimum = 0.0;
        double maximum = 99999.99;

        // Find deal value as percent or amount
        if (value.contains("%")) {
            String percentString = value.substring(value.indexOf("Earn ") + 5, value.indexOf("%"));
            percent = Double.parseDouble(percentString.replaceAll("[^0-9.]+", "")) / 100;
        } else {
            String amountString = value.substring(value.indexOf("Earn $") + 6, value.indexOf(" back"));
            amount = Double.parseDouble(amountString.replaceAll("[^0-9.]+", ""));
            maximum = amount;
        }
        // Find minimum
        if (value.contains("or more")) {
            String minRequire = value.substring(value.toLowerCase().indexOf("purchase of $") + 13,
                    value.toLowerCase().indexOf("or more") - 1);
            minimum = Double.parseDouble(minRequire.replaceAll("[^0-9.]+", ""));
        }

        return new OfferSavingsValues(percent, amount, minimum, maximum);
    }

    static OfferSavingsValues parseCitiBackupValue(String value) throws ParseException {
        // Examples
        // 6% back
        // $20 back

        double percent = 0.0;
        double amount = 0.0;
        double minimum = 0.0;
        double maximum = 99999.99;

        if (value.contains("%")) {
            percent = Double.parseDouble(value.replace("%", "")) / 100;
        }
        if (value.contains("$")) {
            amount = Double.parseDouble(value.replace("$", ""));
        }

        return new OfferSavingsValues(percent, amount, minimum, maximum);

    }

    static LocalDate parseCitiExpiration(String expirationText) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return LocalDate.parse(expirationText.replace("Exp. ", ""), formatter);
    }

}
