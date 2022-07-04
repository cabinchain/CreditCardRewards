package bankoffers;

//import java.util.*;
import java.io.*;
import java.text.ParseException;
//import java.text.SimpleDateFormat;

// To do
// 1) Change how this waits for page to load (in bank class)
// 2) test bank class with BoA
// 3) Find all xpaths for next bank

public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException, IOException {

        BankScraper bankOfAmerica = BankScraper.forBankOfAmerica();
        bankOfAmerica.scrape();

        // String DRIVER_NAME = "webdriver.chrome.driver";
        // String DRIVER_PATH =
        // "C:\\Users\\kevin\\OneDrive\\Documents\\Programming\\Projects\\Libraries\\chromedriver_win32\\chromedriver.exe";
        // String LOGIN_URL = "https://www.bankofamerica.com";
        // String USERNAME_FIELD = "//*[@id=\"onlineId1\"]";
        // String PASSWORD_FIELD = "//*[@id=\"passcode1\"]";
        // String LOGIN_BUTTON = "//*[@id=\"signIn\"]";
        // String OFFERS_URL = "https://secure.bankofamerica.com/customer-deals/";
        // String ADD_DEAL_ELEMENT = "//*/div/div/div[1]/div[4]/div[1]/a";
        // String VENDOR_NAME_ELEMENT = "//*[@id=\"company_product_desc_ada\"]/img";
        // String VENDOR_NAME_ATTRIBUTE = "alt";
        // String VALUE_ELEMENT =
        // "//*[@id=\"bamdAvailableDeals\"]/div[7]/div/div[1]/div[4]/div[1]/span";
        // String VALUE_ATTRIBUTE = "innerHTML";
        // String EXPIRATION_ELEMENT =
        // "//*[@id=\"bamdAvailableDeals\"]/div[7]/div/div[1]/div[4]/div[2]/p[1]";
        // String EXPIRATION_ATTRIBUTE = "innerHTML";

        // // Initialize all drivers
        // Console console = System.console();
        // System.setProperty(DRIVER_NAME, DRIVER_PATH);
        // // configure options parameter to Chrome driver
        // ChromeOptions o = new ChromeOptions();
        // // add Incognito parameter
        // o.addArguments("--incognito");
        // WebDriver driver = new ChromeDriver(o);
        // // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // // Login and navigate to deals
        // driver.manage().window().maximize();
        // driver.get(LOGIN_URL);
        // WebElement usernameElement = driver.findElement(By.xpath(USERNAME_FIELD));
        // WebElement passwordElement = driver.findElement(By.xpath(PASSWORD_FIELD));
        // WebElement login = driver.findElement(By.xpath(LOGIN_BUTTON));
        // String usernameInput = new String(console.readLine("Username:"));
        // usernameElement.sendKeys(usernameInput);
        // String passwordInput = new String(console.readPassword("Password:"));
        // passwordElement.sendKeys(passwordInput);
        // login.click();
        // Thread.sleep(1000);
        // // Test for wrong page
        // driver.get(OFFERS_URL);
        // Thread.sleep(5000);
        // // wait.until(EC.presence_of_element_located((By.className("deal-logo"))));
        // WebElement popup = driver.findElement(By.className("spa-close-x"));
        // popup.click();

        // // Create list of buttons to add deal. Deals previously added not included.
        // List<WebElement> addDeals = driver.findElements(By.xpath(ADD_DEAL_ELEMENT));
        // for (WebElement deal : addDeals) {
        // System.out.println("Add deal link:" + deal.getAttribute("href") + " ");
        // // addDeals.click();
        // }

        // // Create list of elements representing each deal
        // List<WebElement> vendorElements =
        // driver.findElements(By.xpath(VENDOR_NAME_ELEMENT));
        // List<WebElement> valueElements =
        // driver.findElements(By.xpath(VALUE_ELEMENT));
        // List<WebElement> expirationElements =
        // driver.findElements(By.xpath(EXPIRATION_ELEMENT));

        // System.out.println(vendorElements.size());
        // for (int i = 0; i < vendorElements.size(); i++) {

        // // Vendor name
        // String vendor = vendorElements.get(i).getAttribute(VENDOR_NAME_ATTRIBUTE);

        // // Value of deal as percent or $ amount
        // String value = valueElements.get(i).getAttribute(VALUE_ATTRIBUTE);
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

        // // Expiration date
        // String expirationText =
        // expirationElements.get(i).getAttribute(EXPIRATION_ATTRIBUTE).replace("Exp.",
        // "");
        // SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        // Date expirationDate = formatter.parse(expirationText);

        // System.out.println("Img=" + vendor + " Value=" + value + " " + percent + " "
        // + amount + " Exp="
        // + expirationText + " " + expirationDate);

        // // Create deal? or insert to database?
        // }
        // Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");

    }
}