package bankoffers;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

// To do
// DONE Need to consider how to not duplicate offers when running a 2nd time. We can base it on if the button has been pressed? Or search if there's a duplicate.
// DONE - some deals update with later expiration dates... search and if new exp date, then update? otherwise leave alone?
// DONE Search function (search by one name)
// DONE Function to mark off a deal as used
// DONE Savings function ("how much savings is this") - Must specify ID? (done in offer class)
// DONE Change iteration of Offers into its own method that accepts the 4 lists and iterates those. Pass in strings using Java Streams Map
// 1) Change how this waits for page to load (in bank class)
// 3) Find all xpaths for Citi and Chase
// 8) Create class to hold values (passed from parse function)
// 9) Exception: Create custom, throw that in each parse method, then in the createOffers loop, try to create offer and catch custom exception
//  try{creating an offer} catch{exception for parse methods} finally {close any streams}
// 10) Make menu for user to interact with sql data
// 11) Date added field to database and to write function ("offer" objects do not need date added field)
// REPLACE CONSOLE WITH SCANNER

// List of questions:
// 1) Should write do 1 offer or an array? DO ARRAY
// 2) Why do people do List<> list = new ArrayList<>? MORE GENERIC LESS DETAIL FOR USER TO MESS UP ARRAYLIST
// 3) How are these below unit tests? FINE
// 4) How's my folder structure? Where do unit tests go? WHATERVR

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
        Console console = System.console();

        int menu = 99;
        while (menu > 0) {
            System.out.println("MENU OPTIONS:");
            System.out.println("1) Scrape Bank");
            System.out.println("2) Search Offers");
            System.out.println("3) Use Offer");
            System.out.println("0) Exit");
            String menuString = new String(console.readLine("Selection: "));
            try {
                menu = Integer.parseInt(menuString);
            } catch (NumberFormatException e) {
                menu = 99;
            }
            switch (menu) {
                case 0:
                    break;
                case 1:
                    // Select Bank
                    BankScraper selectedBank;
                    List<Offer> scrapedOffers;
                    int bank = 0;
                    System.out.println("BANK OPTIONS:");
                    System.out.println("1) Bank of America");
                    System.out.println("2) American Express");
                    String bankString = new String(console.readLine("Selection: "));
                    try {
                        bank = Integer.parseInt(bankString);
                    } catch (NumberFormatException e) {
                        bank = 0;
                    }
                    switch (bank) {
                        case 1:
                            selectedBank = BankScraper.forBankOfAmerica();
                            break;
                        case 2:
                            selectedBank = BankScraper.forAmericanExpress();
                            break;
                        default:
                            System.out.println("Please enter a valid option. Defaulted to Bank of America.");
                            selectedBank = BankScraper.forBankOfAmerica();
                            break;
                    }
                    scrapedOffers = selectedBank.scrape();
                    for (Offer next : scrapedOffers) {
                        dbrw.write(next);
                    }
                    break;
                case 2:
                    // Search offer
                    String searchString = new String(console.readLine("Search for: "));
                    dbrw.displayResults(dbrw.searchByVendor(searchString));
                    break;
                case 3:
                    // Use offer
                    int offerID;
                    String offerIDString = new String(console.readLine("Select Offer ID: "));
                    try {
                        offerID = Integer.parseInt(offerIDString);
                    } catch (NumberFormatException e) {
                        offerID = 0;
                    }
                    Offer o = dbrw.selectOffer(offerID);
                    if (o != null) {
                        int amount;
                        System.out.println(o.toString());
                        String amountString = new String(console.readLine("Amount to spend: "));
                        try {
                            amount = Integer.parseInt(amountString);
                        } catch (NumberFormatException e) {
                            amount = 0;
                        }
                        System.out.println("Amount saved: " + dbrw.checkOffer(offerID, amount));
                        String confirmUseString = new String(console.readLine("Confirm use of deal (y/n): "));
                        if (confirmUseString.compareTo("y") == 0) {
                            dbrw.useOffer(offerID, amount);
                            System.out.println("Offer # " + offerID + " used.");
                        } else {
                            System.out.println("No offers used.");
                        }
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option.");
            }
        }

        System.out.println("Thank you! Goodbye!");
        dbrw.close();
    }
}