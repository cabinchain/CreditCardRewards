package bankoffers;

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
// Date added field to database and to write function ("offer" objects do not need date added field)
// Add amount spent field so we can calculate % savings later
// Implement Citibank's Scraper as a different Class that implements BankScraper
// Implement Chase as a different class
// Capital one appears to block use of autmomated browser actions
// Turn on and test all the button pressing

// List of questions:
// Should I use more strings and create the PreparedStatement in each DBReadWrite method? That way we can piece together parts of the statement like "AND Date_Used = null"
//      Or should this just be a stored procedure? - Move the PreparedStatement creation into methods and decide from there.
// Look up builder pattern for making offer so long list of parameters don't get messed up (don't do this by hand, use protocall buffer)

public class Main {

    public static void main(String[] args) throws Exception {

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
                    System.out.println("3) Citibank");
                    System.out.println("0) Exit");
                    int bank = inputs.nextInt();
                    inputs.nextLine();
                    switch (bank) {
                        case 1:
                            selectedBank = StandardBankScraper.forBankOfAmerica();
                            break;
                        case 2:
                            selectedBank = StandardBankScraper.forAmericanExpress();
                            break;
                        case 3:
                            selectedBank = new CitiBankScraper();
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