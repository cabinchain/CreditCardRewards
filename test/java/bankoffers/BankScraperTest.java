package bankoffers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BankScraperTest {

    @Test
    public void parseBOAVendor() {
        // BOA vendor name pulled from image name in the format of "<vendor name> Logo"
        assertTrue("Test company name".compareTo(BankScraper.parseBOAVendor("Test company name Logo")) == 0);
    }

    double[] BOAValuesPercent = BankScraper.parseBOAValue("10%");
    double[] BOAValuesAmount = BankScraper.parseBOAValue("$10");
    // returns in order of percent, amount, min, max

    @Test
    public void parseBOAValuePercent() {
        assertEquals(0.1, BOAValuesPercent[0], 0.001);
        assertEquals(0.0, BOAValuesPercent[1], 0.001);
        assertEquals(0.0, BOAValuesPercent[2], 0.001);
        assertEquals(99999.99, BOAValuesPercent[3], 0.001);
    }

    @Test
    public void parseBOAValueAmount() {
        assertEquals(0.0, BOAValuesAmount[0], 0.001);
        assertEquals(10.0, BOAValuesAmount[1], 0.001);
        assertEquals(0.0, BOAValuesAmount[2], 0.001);
        assertEquals(99999.99, BOAValuesAmount[3], 0.001);
    }

    Date expDate = new GregorianCalendar(2022, Calendar.AUGUST, 24).getTime();

    @Test
    public void parseBOAExpiration() throws ParseException {

        assertTrue(expDate.compareTo(BankScraper.parseBOAExpiration("Exp. 08/24/22")) == 0);
    }

}
