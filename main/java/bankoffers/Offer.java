package bankoffers;

import java.util.*;

public class Offer {
    private final String bank;
    private final String vendor;
    private final OfferSavingsValues values;
    // private final double percent;
    // private final double amount;
    // private final double minimum; // minimum spent to avail
    // private final double maximum; // maximum reward
    private final Date expiration;

    public Offer(String bank, String vendor,
            /* double percent, double amount, double min, double max, */ OfferSavingsValues values, Date expiration) {
        this.bank = bank;
        this.vendor = vendor;
        this.values = values;
        // this.percent = percent;
        // this.amount = amount;
        // this.minimum = min;
        // this.maximum = max;
        this.expiration = expiration;
    }

    public Boolean hasExpired() {
        Date currentDate = new Date();
        return (currentDate.compareTo(expiration) > 0); // returns greater than 0 if current Date after the expiration
    }

    public double expectedSavings(double price) {
        double percent = values.getPercent();
        double amount = values.getAmount();
        double minimum = values.getMinimum();
        double maximum = values.getMaximum();
        if (percent != 0) {
            return (percent * price > maximum) ? maximum : percent * price;
        } else if (amount > 0 && price > minimum) {
            return amount;
        } else
            return 0.0;
    }

    public String toString() {
        return ("bank:" + bank + ", Vendor:" + vendor + ", Percent:" + values.getPercent() + ", Amount:"
                + values.getAmount() + ", Minimum:" + values.getMinimum() + ", Maximum:" + values.getMaximum()
                + ", Expiration:" + expiration);
    }

    // getters
    public String getBank() {
        return bank;
    }

    public String getVendor() {
        return vendor;
    }

    public Double getPercent() {
        return values.getPercent();
    }

    public Double getAmount() {
        return values.getAmount();
    }

    public Double getMinimum() {
        return values.getMinimum();
    }

    public Double getMaximum() {
        return values.getMaximum();
    }

    public Date getExpiration() {
        return expiration;
    }

    public OfferSavingsValues getSavingsValues() {
        return values;
    }

}
