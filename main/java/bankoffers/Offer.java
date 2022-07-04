package bankoffers;

import java.util.*;

public class Offer {
    private String card;
    private String vendor;
    private Double percent;
    private Double amount;
    private Double minimum;
    private Date expiration;

    public Offer(String card, String vendor, Double percent, Double amount, Double minimum, Date expiration) {
        this.card = card;
        this.vendor = vendor;
        this.percent = percent;
        this.amount = amount;
        this.minimum = minimum;
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

    public Date getExpiration() {
        return expiration;
    }

}
