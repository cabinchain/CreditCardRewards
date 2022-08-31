package bankoffers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

interface BankScraper {

    public List<Offer> scrape() throws InterruptedException, ParseException, IOException, SQLException;

}
