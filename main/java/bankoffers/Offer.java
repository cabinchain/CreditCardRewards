package bankoffers;

import java.util.*;

public class Offer {
    private final String card;
    private final String vendor;
    private final Double percent;
    private final Double amount;
    private final Double minimum; // minimum spent to avail
    private final Double maximum; // maximum reward
    private final Date expiration;

    public Offer(String card, String vendor, Double percent, Double amount, Double min, Double max, Date expiration) {
        this.card = card;
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

    public String createJSON() {
        return "";
    }

    public Double expectedSavings(Double price) {
        if (percent != null) {
            return percent * price;
        } else if (amount != null && price > minimum) {
            return amount;
        } else
            return 0.0;
    }

    public String toString() {
        return ("Card:" + card + " Vendor:" + vendor + " Percent:" + percent + " Amount:" + amount + " Minimum:"
                + minimum + " Maximum:" + maximum + " Expiration:" + expiration);
    }

    // getters
    public String getCard() {
        return card;
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
