package bankoffers;

// import java.util.*;
// import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OfferTest {

    Date exp1 = new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime();
    Date exp2 = new GregorianCalendar(2099, Calendar.FEBRUARY, 11).getTime();
    private final Offer offer1 = new Offer("bank", "vendor", 0.0, 20.0, 100.0, 999999.99, exp1);
    private final Offer offer2 = new Offer("bank", "vendor", 0.15, 0.0, 0.0, 30.0, exp2);

    @Test
    public void hasExpired1() {
        // offer1 has exp1 with date 11/2/2020
        assertTrue(offer1.hasExpired());
    }

    @Test
    public void hasExpired2() {
        // offer1 has exp2 with date 11/2/2099
        assertFalse(offer2.hasExpired());
    }

    @Test
    public void expectedSavingsAmount() {
        // minimum required spending - test while hitting minimum
        assertEquals(20.0, offer1.expectedSavings(110.0), 0.001);
    }

    @Test
    public void expectedSavingsAmount_Min() {
        // minimum required spending - test while under minimum
        assertEquals(0.0, offer1.expectedSavings(10.0), 0.001);
    }

    @Test
    public void expectedSavingsPercent() {
        // percent, with maximum reward, text % amount
        assertEquals(15.0, offer2.expectedSavings(100.0), 0.001);
    }

    @Test
    public void expectedSavingsPercent_Max() {
        // percent, with maximum reward - test that we cannot exceed max
        assertEquals(30.0, offer2.expectedSavings(220.0), 0.001);
    }

}
