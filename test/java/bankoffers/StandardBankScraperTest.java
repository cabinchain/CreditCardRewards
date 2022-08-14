package bankoffers;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.time.LocalDate;

public class StandardBankScraperTest {

    @Test
    public void parseBOAVendor() {
        // BOA vendor name pulled from image name in the format of "<vendor name> Logo"
        assertTrue("Test company name".equals(StandardBankScraper.parseBOAVendor("Test company name Logo")));
    }

    OfferSavingsValues BOAValuesPercent = StandardBankScraper.parseBOAValue("10%");
    OfferSavingsValues BOAValuesAmount = StandardBankScraper.parseBOAValue("$10");
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

    @Test
    public void parseBOAExpiration() throws ParseException {
        LocalDate BOAexpDate = LocalDate.of(2022, 8, 24);
        assertTrue(BOAexpDate.equals(StandardBankScraper.parseBOAExpiration("Exp. 08/24/22")));
    }

    @Test
    public void parseAMEXVendor() {
        // AMEX vendor name pulled from image name in the format of "<vendor name> Logo"
        assertTrue("Test company name".equals(StandardBankScraper.parseAMEXVendor("Test company name")));
    }

    // LOTS OF AMEX VALUE TESTS
    @Test
    public void parseAMEXValue_PercentMax() throws ParseException {
        OfferSavingsValues AMEXValuesPercentMax = StandardBankScraper
                .parseAMEXValue("Get 2% back on purchases, up to a total of $250");
        assertEquals(0.02, AMEXValuesPercentMax.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMax.getAmount(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMax.getMinimum(), 0.001);
        assertEquals(250.0, AMEXValuesPercentMax.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_AmountMin() throws ParseException {
        OfferSavingsValues AMEXValuesAmountMin = StandardBankScraper
                .parseAMEXValue("Spend $125 or more, get $25 back. Up to 2 times");
        assertEquals(0.0, AMEXValuesAmountMin.getPercent(), 0.001);
        assertEquals(25.0, AMEXValuesAmountMin.getAmount(), 0.001);
        assertEquals(125.0, AMEXValuesAmountMin.getMinimum(), 0.001);
        assertEquals(25.0, AMEXValuesAmountMin.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_PercentMinMax() throws ParseException {
        OfferSavingsValues AMEXValuesPercentMinMax = StandardBankScraper
                .parseAMEXValue("Spend $15 or more, get 20% back, up to a total of $40");
        assertEquals(0.20, AMEXValuesPercentMinMax.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMinMax.getAmount(), 0.001);
        assertEquals(15, AMEXValuesPercentMinMax.getMinimum(), 0.001);
        assertEquals(40.0, AMEXValuesPercentMinMax.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXValue_PercentMaxEtc() throws ParseException {
        OfferSavingsValues AMEXValuesPercentMaxEtc = StandardBankScraper
                .parseAMEXValue("Get 50% back for up to 3 months, up to a total of $55. Enroll by 10/1/2022.");
        assertEquals(0.50, AMEXValuesPercentMaxEtc.getPercent(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMaxEtc.getAmount(), 0.001);
        assertEquals(0.0, AMEXValuesPercentMaxEtc.getMinimum(), 0.001);
        assertEquals(55.0, AMEXValuesPercentMaxEtc.getMaximum(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void parseAMEXValue_Exception() throws ParseException {
        StandardBankScraper.parseAMEXValue("Keyword 'G-E-T' is not in this string");
    }

    @Test
    public void parseAMEXValue_PercentMinEtc() throws ParseException {
        OfferSavingsValues AMEXValuesAmountMinEtc = StandardBankScraper
                .parseAMEXValue("Spend $75+, get $5 back, up to 2X times (total of $10) after you Dine Small");
        assertEquals(0.0, AMEXValuesAmountMinEtc.getPercent(), 0.001);
        assertEquals(5.0, AMEXValuesAmountMinEtc.getAmount(), 0.001);
        assertEquals(0.0, AMEXValuesAmountMinEtc.getMinimum(), 0.001);
        assertEquals(5.0, AMEXValuesAmountMinEtc.getMaximum(), 0.001);
    }

    @Test
    public void parseAMEXExpiration_Date() throws ParseException {
        LocalDate AMEXexpDate = LocalDate.of(2022, 8, 24);
        assertTrue(AMEXexpDate.equals(StandardBankScraper.parseAMEXExpiration("08/24/22")));
    }

    // Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Test
    public void parseAMEXExpiration_InXDays() throws ParseException {
        LocalDate in5days = LocalDate.now().plusDays(5);
        LocalDate testDate = StandardBankScraper.parseAMEXExpiration("Expires in 5 days");
        assertTrue(in5days.equals(testDate));
    }

    @Test
    public void parseAMEXExpiration_Today() throws ParseException {
        LocalDate today = LocalDate.now();
        LocalDate testDate = StandardBankScraper.parseAMEXExpiration("Expires Today");
        assertTrue(today.equals(testDate));
    }

    @Test
    public void parseAMEXExpiration_Tomorrow() throws ParseException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate testDate = StandardBankScraper.parseAMEXExpiration("Expires tomorrow");
        assertTrue(tomorrow.equals(testDate));
    }

}
