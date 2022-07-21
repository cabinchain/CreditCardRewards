package bankoffers;

//import java.util.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
//import java.text.SimpleDateFormat;

// To do
// 1) Change how this waits for page to load (in bank class)
// 2) test bank class with BoA
// 3) Find all xpaths for next bank

public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException, IOException, SQLException {

        // BankScraper bankOfAmerica = BankScraper.forBankOfAmerica();
        // bankOfAmerica.scrape();

        BankScraper amex = BankScraper.forAmericanExpress();
        amex.scrape();

    }
}