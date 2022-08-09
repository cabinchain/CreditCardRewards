package bankoffers;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
//import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BankScraperTest {

    @Test
    public void parseBOAVendor() {
        // BOA vendor name pulled from image name in the format of "<vendor name> Logo"
        assertTrue("Test company name".equals(BankScraper.parseBOAVendor("Test company name Logo")));
    }

    OfferSavingsValues BOAValuesPercent = BankScraper.parseBOAValue("10%");
    OfferSavingsValues BOAValuesAmount = BankScraper.parseBOAValue("$10");
    // returns in order of percent, amount, min, max

    @Test
    public void parseBOAValue_Percent() {
        assertEquals(0.1, BOAValuesPercent.getPercent(), 0.001);
        assertEquals(0.0, BOAValuesPercent.getAmount(), 0.001);
        assertEquals(0.0, BOAValuesPercent.getMinimum(), 0.001);
        assertEquals(99999.99, BOAValuesPercent.getMaximum(), 0.001);
    }

    @Test
    public void parseBOAValue_Amount() {
        assertEquals(0.0, BOAValuesAmount.getPercent(), 0.001);
        assertEquals(10.0, BOAValuesAmount.getAmount(), 0.001);
        assertEquals(0.0, BOAValuesAmount.getMinimum(), 0.001);
        assertEquals(99999.99, BOAValuesAmount.getMaximum(), 0.001);
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
    OfferSavingsValues AMEXValuesPercentMax = BankScraper
            .parseAMEXValue("Get 2% back on purchases, up to a total of $250");
    OfferSavingsValues AMEXValuesAmountMin = BankScraper
            .parseAMEXValue("Spend $125 or more, get $25 back. Up to 2 times");
    OfferSavingsValues AMEXValuesPercentMinMax = BankScraper
            .parseAMEXValue("Spend $15 or more, get 20% back, up to a total of $40");
    OfferSavingsValues AMEXValuesPercentMaxEtc = BankScraper
            .parseAMEXValue("Get 50% back for up to 3 months, up to a total of $55. Enroll by 10/1/2022.");
    OfferSavingsValues AMEXValuesAmountMinEtc = BankScraper
            .parseAMEXValue("Spend $75+, get $5 back, up to 2X times (total of $10) after you Dine Small");
    // returns in order of percent, amount, min, max

    @Test
    public void parseAMEXValue_PercentMax() {
        assertEquals(0.02, AMEXValuesPercentMax.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMax.getAmount(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMax.getMinimum(), 0.001);
        assertEquals(250.0, AMEXValuesPercentMax.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_AmountMin() {
        assertEquals(0.0, AMEXValuesAmountMin.getPercent(), 0.001);
        assertEquals(25.0, AMEXValuesAmountMin.getAmount(), 0.001);
        assertEquals(125, AMEXValuesAmountMin.getMinimum(), 0.001);
        assertEquals(99999.99, AMEXValuesAmountMin.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_PercentMinMax() {
        assertEquals(0.20, AMEXValuesPercentMinMax.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMinMax.getAmount(), 0.001);
        assertEquals(15, AMEXValuesPercentMinMax.getMinimum(), 0.001);
        assertEquals(40.0, AMEXValuesPercentMinMax.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_PercentMaxEtc() {
        assertEquals(0.50, AMEXValuesPercentMaxEtc.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMaxEtc.getAmount(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMaxEtc.getMinimum(), 0.001);
        assertEquals(55.0, AMEXValuesPercentMaxEtc.getMaximum(), 0.001);
    }

    // @Test
    // public void parseAMEXValue_PercentMinEtc() {
    // assertEquals(0.0, AMEXValuesAmountMinEtc.getPercent(), 0.001);
    // assertEquals(5.0, AMEXValuesAmountMinEtc.getAmount(), 0.001);
    // assertEquals(0.0, AMEXValuesAmountMinEtc.getMinimum(), 0.001);
    // assertEquals(0.0, AMEXValuesAmountMinEtc.getMaximum(), 0.001);
    // }

    Date AMEXexpDate = new GregorianCalendar(2022, Calendar.AUGUST, 24).getTime();

    Calendar today = new GregorianCalendar();

    // Date AMEXexpDate_tomorrow = today.clone().add(Calendar.DATE, 1);
    // Date AMEXexpDate_in5Days = today.clone().add(Calendar.DATE, 5);

    // @Test
    // public void parseAMEXExpiration_Date() throws ParseException {

    // assertTrue(AMEXexpDate.compareTo(BankScraper.parseAMEXExpiration("08/24/22"))
    // == 0);
    // }

    // @Test
    // public void parseAMEXExpiration_InXDays() throws ParseException {

    // assertTrue(AMEXexpDate_tomorrow.compareTo(BankScraper.parseAMEXExpiration("Expires
    // in 5 days")) == 0);
    // }

    // @Test
    // public void parseAMEXExpiration_Tomorrow() throws ParseException {

    // assertTrue(AMEXexpDate_in5Days.compareTo(BankScraper.parseAMEXExpiration("Expires
    // Tomorrow")) == 0);
    // }

}
