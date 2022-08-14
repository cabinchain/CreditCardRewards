package bankoffers;

// import java.util.*;
// import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class OfferTest {

    LocalDate exp1 = LocalDate.of(2020, 2, 11);
    LocalDate exp2 = LocalDate.of(2099, 2, 11);
    private final Offer offer1 = new Offer("bank", "vendor", 0.0, 20.0, 100.0, 999999.99, exp1);
    private final Offer offer2 = new Offer("bank", "vendor", 0.15, 0.0, 0.0, 30.0, exp2);

    @Test
    public void hasExpired_Expired() {
        // offer1 has exp1 with date 11/2/2020
        assertTrue(offer1.hasExpired());
    }

    @Test
    public void hasExpired_NotExpired() {
        // offer1 has exp2 with date 11/2/2099
        assertFalse(offer2.hasExpired());
    }

    @Test
    public void expectedSavings_Amount() {
        // minimum required spending - test while hitting minimum
        assertEquals(20.0, offer1.expectedSavings(110.0), 0.001);
    }

    @Test
    public void expectedSavings_AmountMin() {
        // minimum required spending - test while under minimum
        assertEquals(0.0, offer1.expectedSavings(10.0), 0.001);
    }

    @Test
    public void expectedSavings_Percent() {
        // percent, with maximum reward, text % amount
        assertEquals(15.0, offer2.expectedSavings(100.0), 0.001);
    }

    @Test
    public void expectedSavings_PercentMax() {
        // percent, with maximum reward - test that we cannot exceed max
        assertEquals(30.0, offer2.expectedSavings(220.0), 0.001);
    }

}
