package bankoffers;

import java.util.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;

interface BankScraper {

    public List<Offer> scrape() throws InterruptedException, ParseException, IOException, SQLException;

    // Citi Parsers

    // Chase Parsers

}
