package bankoffers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDateTime;
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
    public void parseBOAValue_Percent() {
        assertEquals(0.1, BOAValuesPercent[0], 0.001);
        assertEquals(0.0, BOAValuesPercent[1], 0.001);
        assertEquals(0.0, BOAValuesPercent[2], 0.001);
        assertEquals(99999.99, BOAValuesPercent[3], 0.001);
    }

    @Test
    public void parseBOAValue_Amount() {
        assertEquals(0.0, BOAValuesAmount[0], 0.001);
        assertEquals(10.0, BOAValuesAmount[1], 0.001);
        assertEquals(0.0, BOAValuesAmount[2], 0.001);
        assertEquals(99999.99, BOAValuesAmount[3], 0.001);
    }

    Date BOAexpDate = new GregorianCalendar(2022, Calendar.AUGUST, 24).getTime();

    @Test
    public void parseBOAExpiration() throws ParseException {

        assertTrue(BOAexpDate.compareTo(BankScraper.parseBOAExpiration("Exp. 08/24/22")) == 0);
    }

    @Test
    public void parseAMEXVendor() {
        // AMEX vendor name pulled from image name in the format of "<vendor name> Logo"
        assertTrue("Test company name".compareTo(BankScraper.parseAMEXVendor("Test company name")) == 0);
    }

    // LOTS OF AMEX VALUE TESTS
    double[] AMEXValuesPercentMax = BankScraper.parseAMEXValue("Get 2% back on purchases, up to a total of $250");
    double[] AMEXValuesAmountMin = BankScraper.parseAMEXValue("Spend $125 or more, get $25 back. Up to 2 times");
    double[] AMEXValuesPercentMinMax = BankScraper
            .parseAMEXValue("Spend $15 or more, get 20% back, up to a total of $40");
    double[] AMEXValuesPercentMaxEtc = BankScraper
            .parseAMEXValue("Get 50% back for up to 3 months, up to a total of $55. Enroll by 10/1/2022.");
    double[] AMEXValuesAmountMinEtc = BankScraper
            .parseAMEXValue("Spend $75+, get $5 back, up to 2X times (total of $10) after you Dine Small");
    // returns in order of percent, amount, min, max

    @Test
    public void parseAMEXValue_PercentMax() {
        assertEquals(0.02, AMEXValuesPercentMax[0], 0.001);
        assertEquals(0.0, AMEXValuesPercentMax[1], 0.001);
        assertEquals(0.0, AMEXValuesPercentMax[2], 0.001);
        assertEquals(250.0, AMEXValuesPercentMax[3], 0.001);
    }

    @Test
    public void parseAMEXValue_AmountMin() {
        assertEquals(0.0, AMEXValuesAmountMin[0], 0.001);
        assertEquals(25.0, AMEXValuesAmountMin[1], 0.001);
        assertEquals(125, AMEXValuesAmountMin[2], 0.001);
        assertEquals(99999.99, AMEXValuesAmountMin[3], 0.001);
    }

    Date AMEXexpDate = new GregorianCalendar(2022, Calendar.AUGUST, 24).getTime();

    Calendar today = new GregorianCalendar();

    Date AMEXexpDate_tomorrow = today.clone().add(Calendar.DATE, 1);
    Date AMEXexpDate_in5Days = today.clone().add(Calendar.DATE, 5);

    @Test
    public void parseAMEXExpiration_Date() throws ParseException {

        assertTrue(AMEXexpDate.compareTo(BankScraper.parseAMEXExpiration("08/24/22")) == 0);
    }

    @Test
    public void parseAMEXExpiration_InXDays() throws ParseException {

        assertTrue(AMEXexpDate_tomorrow.compareTo(BankScraper.parseAMEXExpiration("Expires in 5 days")) == 0);
    }

    @Test
    public void parseAMEXExpiration_Tomorrow() throws ParseException {

        assertTrue(AMEXexpDate_in5Days.compareTo(BankScraper.parseAMEXExpiration("Expires Tomorrow")) == 0);
    }

}
