package bankoffers;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.time.LocalDate;

public class CitiBankScraperTest {

    @Test
    public void parseCitiVendor() {
        // Luckily CitiBank's vendors are exactly as see in html.
        assertTrue("Test company name".equals(CitiBankScraper.parseCitiVendor("Test company name")));
    }

    // LOTS OF VALUE TESTS

    @Test
    public void parseCitiValue_Percent() throws ParseException {
        OfferSavingsValues CitiValuesPercent = CitiBankScraper
                .parseCitiValue("Earn 20% back on any purchase.");
        assertEquals(0.20, CitiValuesPercent.getPercent(), 0.001);
        assertEquals(0.0, CitiValuesPercent.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesPercent.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesPercent.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_Amount() throws ParseException {
        OfferSavingsValues CitiValuesAmount = CitiBankScraper
                .parseCitiValue("Earn $10 back on any purchase.");
        assertEquals(0.0, CitiValuesAmount.getPercent(), 0.001);
        assertEquals(10.0, CitiValuesAmount.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesAmount.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesAmount.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_AmountMin() throws ParseException {
        OfferSavingsValues CitiValuesAmountMin = CitiBankScraper
                .parseCitiValue("Earn $9.99 back on a purchase of $50 or more.");
        assertEquals(0.0, CitiValuesAmountMin.getPercent(), 0.001);
        assertEquals(10.0, CitiValuesAmountMin.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesAmountMin.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesAmountMin.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_PercentMax() throws ParseException {
        OfferSavingsValues CitiValuesPercentMax = CitiBankScraper
                .parseCitiValue("Earn 10% back on any purchase, up to a total of $25");
        assertEquals(0.02, CitiValuesPercentMax.getPercent(), 0.001);
        assertEquals(0.0, CitiValuesPercentMax.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesPercentMax.getMinimum(), 0.001);
        assertEquals(250.0, CitiValuesPercentMax.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_AmountMin() throws ParseException {
        OfferSavingsValues CitiValuesAmountMin = CitiBankScraper
                .parseCitiValue("Spend $50 with LugLess on your first luggage shipment and get a $20 statement credit");
        assertEquals(0.0, CitiValuesAmountMin.getPercent(), 0.001);
        assertEquals(20.0, CitiValuesAmountMin.getAmount(), 0.001);
        assertEquals(50.0, CitiValuesAmountMin.getMinimum(), 0.001);
        assertEquals(20.0, CitiValuesAmountMin.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_PercentMin() throws ParseException {
        OfferSavingsValues CitiValuesPercentMin = CitiBankScraper
                .parseCitiValue("Earn 10% back on a purchase of $100 or more.");
        assertEquals(0.10, CitiValuesPercentMin.getPercent(), 0.001);
        assertEquals(0.0, CitiValuesPercentMin.getAmount(), 0.001);
        assertEquals(100, CitiValuesPercentMin.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesPercentMin.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_PercentMinMax() throws ParseException {
        OfferSavingsValues CitiValuesPercentMinMax = CitiBankScraper
                .parseCitiValue("Spend $15 or more, get 20% back, up to a total of $40");
        assertEquals(0.20, CitiValuesPercentMinMax.getPercent(), 0.001);
        assertEquals(0.0, CitiValuesPercentMinMax.getAmount(), 0.001);
        assertEquals(15, CitiValuesPercentMinMax.getMinimum(), 0.001);
        assertEquals(40.0, CitiValuesPercentMinMax.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_PercentMaxEtc() throws ParseException {
        OfferSavingsValues CitiValuesPercentMaxEtc = CitiBankScraper
                .parseCitiValue(
                        "Earn $10 back on your next purchase of $20 or more at bp and Amoco when you use the BPme app!");
        assertEquals(0.0, CitiValuesPercentMaxEtc.getPercent(), 0.001);
        assertEquals(10.0, CitiValuesPercentMaxEtc.getAmount(), 0.001);
        assertEquals(20.0, CitiValuesPercentMaxEtc.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesPercentMaxEtc.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiValue_AmountMinEtc() throws ParseException {
        OfferSavingsValues CitiValuesAmountMinEtc = CitiBankScraper
                .parseCitiValue("Spend $75+, get $5 back, up to 2X times (total of $10) after you Dine Small");
        assertEquals(0.0, CitiValuesAmountMinEtc.getPercent(), 0.001);
        assertEquals(5.0, CitiValuesAmountMinEtc.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesAmountMinEtc.getMinimum(), 0.001);
        assertEquals(5.0, CitiValuesAmountMinEtc.getMaximum(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void parseCitiValue_Exception() throws ParseException {
        CitiBankScraper.parseCitiValue("There are no numbers in this string");
    }

    // Tests backup method (different type of deal text)
    OfferSavingsValues CitiValuesPercent = CitiBankScraper.parseCitiBackupValue("10% back");
    OfferSavingsValues CitiValuesAmount = CitiBankScraper.parseCitiBackupValue("$10 back");

    @Test
    public void parseCitiBackupValue_Percent() {
        assertEquals(0.1, CitiValuesPercent.getPercent(), 0.001);
        assertEquals(0.0, CitiValuesPercent.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesPercent.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesPercent.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiBackupValue_Amount() {
        assertEquals(0.0, CitiValuesAmount.getPercent(), 0.001);
        assertEquals(10.0, CitiValuesAmount.getAmount(), 0.001);
        assertEquals(0.0, CitiValuesAmount.getMinimum(), 0.001);
        assertEquals(99999.99, CitiValuesAmount.getMaximum(), 0.001);
    }

    @Test
    public void parseCitiExpiration() throws ParseException {
        LocalDate CitiexpDate = LocalDate.of(2022, 8, 24);
        assertTrue(CitiexpDate.equals(CitiBankScraper.parseCitiExpiration("Exp. 08/24/22")));
    }

}
