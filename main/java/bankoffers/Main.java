package bankoffers;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

// To do
// Change how this waits for page to load (in bank class)
// For each new bank
//      1) Add new variables in BankScraper constructor
//      2) Update offer object in BankScraper.createOffers()
//      3) Add to menu in main
//      4) Parse functions in BankScraper
//      5) Write parse tests for BankScraper
// 9) Exception: Create custom, throw that in each parse method, then in the createOffers loop, try to create offer and catch custom exception
//  try{creating an offer} catch{exception for parse methods} finally {close any streams}
// 11) Date added field to database and to write function ("offer" objects do not need date added field)
// Add amount spent field so we can calculate % savings later
// REPLACE CONSOLE WITH SCANNER

// List of questions:
// 1) Should write do 1 offer or an array? DO ARRAY

// 6) Should I use more strings and create the PreparedStatement in each DBReadWrite method? That way we can piece together parts of the statement like "AND Date_Used = null"
//      Or should this just be a stored procedure?
// How to do custom exception? Still confused.

public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException, IOException, SQLException {

        DBReadWrite dbrw = new DBReadWrite();
        Scanner inputs = new Scanner(System.in);

        int menu = 99;
        while (menu > 0) {
            System.out.println("MENU OPTIONS:");
            System.out.println("1) Scrape Bank");
            System.out.println("2) Search Offers");
            System.out.println("3) Use Offer");
            System.out.println("0) Exit");
            System.out.print("Selection: ");
            menu = inputs.nextInt();
            inputs.nextLine();// need to consume hanging /n - do for each nextInt()
            switch (menu) {
                case 0:
                    break;
                case 1:
                    // Select Bank
                    BankScraper selectedBank;
                    List<Offer> scrapedOffers;
                    System.out.println("BANK OPTIONS:");
                    System.out.println("1) Bank of America");
                    System.out.println("2) American Express");
                    System.out.println("0) Exit");
                    int bank = inputs.nextInt();
                    inputs.nextLine();
                    switch (bank) {
                        case 1:
                            selectedBank = BankScraper.forBankOfAmerica();
                            break;
                        case 2:
                            selectedBank = BankScraper.forAmericanExpress();
                            break;
                        default:
                            System.out.println("No bank selected, exiting to main menu.");
                            selectedBank = null;
                            break;
                    }
                    try {
                        scrapedOffers = selectedBank.scrape();
                        for (Offer next : scrapedOffers) {
                            dbrw.write(next);
                        }
                    } catch (NullPointerException e) {
                        break;
                    }
                    break;
                case 2:
                    // Search offer
                    System.out.print("Search for: ");
                    String searchString = inputs.nextLine();
                    dbrw.displayResults(dbrw.searchByVendor(searchString));
                    break;
                case 3:
                    // Use offer
                    System.out.print("Select Offer ID: ");
                    int offerID = inputs.nextInt();
                    inputs.nextLine();
                    Offer o = dbrw.selectOffer(offerID);
                    if (o != null) {
                        System.out.println(o.toString());
                        System.out.print("Amount to spend: ");
                        double amount = inputs.nextDouble();
                        inputs.nextLine();
                        System.out.println("Amount saved: " + dbrw.getExpectedSavings(offerID, amount));
                        System.out.print("Confirm use of deal (y/n): ");
                        String confirmUseString = inputs.nextLine();
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
        inputs.close();
        dbrw.close();
    }
}