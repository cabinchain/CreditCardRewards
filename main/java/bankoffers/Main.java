package bankoffers;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

// To do
// 0) Need to consider how to not duplicate offers when running a 2nd time. We can base it on if the button has been pressed? Or search if there's a duplicate.
// 1) Change how this waits for page to load (in bank class)
// 2) test bank class with BoA
// 3) Find all xpaths for next bank

// DONE 4) Search function (search by one name)
// DONE 5) Function to mark off a deal as used
// DONE 6) Savings function ("how much savings is this") - Must specify ID? (done in offer class)
// DONE 7) Change iteration of Offers into its own method that accepts the 4 lists and iterates those. Pass in strings using Java Streams Map
// 8) Create class to hold values (passed from parse function)
// 9) Exception: Create custom, throw that in each parse method, then in the createOffers loop, try to create offer and catch custom exception
//  try{creating an offer} catch{exception for parse methods} finally {close any streams}
// Create menu in main to select actions

// List of questions:
// 1) Should write do 1 offer or an array? DO ARRAY
// 2) Why do people do List<> list = new ArrayList<>? MORE GENERIC LESS DETAIL FOR USER TO MESS UP ARRAYLIST
// 3) How are these below unit tests?
// 4) How's my folder structure? Where do unit tests go? 

// 5) selectOffer, checkOffer, useOffer, am I being redundant?
// 6) Should I use more strings and create the PreparedStatement in each DBReadWrite method? That way we can piece together parts of the statement like "AND Date_Used = null"
//      Or should this just be a stored procedure?

// List of unit tests:
// BankScraper
//      All the parse functions
//      Cannot make unit function for scrape() right? To dependent on other things? See item 7 on Todo
// Offer - DONE
//      Test hasExpired() expectedSavings() toString()
//DBReadWrite
//      Can't really make anything?
//      "In memory"

public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException, IOException, SQLException {

        DBReadWrite dbrw = new DBReadWrite();

        // String usernameInput = new String(console.readLine("Username:"));
        // Prompt for actions

        BankScraper bankOfAmerica = BankScraper.forBankOfAmerica();
        List<Offer> scrapedOffers = bankOfAmerica.scrape();

        // BankScraper amex = BankScraper.forAmericanExpress();
        // List<Offer> scrapedOffers = amex.scrape();

        for (Offer o : scrapedOffers) {
            dbrw.write(o);
        }

        dbrw.displayResults(dbrw.searchByVendor("Sperry"));
        Offer o = dbrw.selectOffer(375);
        System.out.println(o.toString());
        System.out.println(dbrw.checkOffer(394, 150));

        dbrw.close();
    }
}