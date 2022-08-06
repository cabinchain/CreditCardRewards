package bankoffers;

import java.util.*;

public class Offer {
    private final String bank;
    private final String vendor;
    private final double percent;
    private final double amount;
    private final double minimum; // minimum spent to avail
    private final double maximum; // maximum reward
    private final Date expiration;

    public Offer(String bank, String vendor, double percent, double amount, double min, double max, Date expiration) {
        this.bank = bank;
        this.vendor = vendor;
        this.percent = percent;
        this.amount = amount;
        this.minimum = min;
        this.maximum = max;
        this.expiration = expiration;
    }

    public Boolean hasExpired() {
        Date currentDate = new Date();
        return (currentDate.compareTo(expiration) > 0); // returns greater than 0 if current Date after the expiration
    }

    public double expectedSavings(double price) {
        if (percent != 0) {
            return (percent * price > maximum) ? maximum : percent * price;
        } else if (amount > 0 && price > minimum) {
            return amount;
        } else
            return 0.0;
    }

    public String toString() {
        return ("bank:" + bank + ", Vendor:" + vendor + ", Percent:" + percent + ", Amount:" + amount + ", Minimum:"
                + minimum + ", Maximum:" + maximum + ", Expiration:" + expiration);
    }

    // getters
    public String getBank() {
        return bank;
    }

    public String getVendor() {
        return vendor;
    }

    public Double getPercent() {
        return percent;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getMinimum() {
        return minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public Date getExpiration() {
        return expiration;
    }

}
