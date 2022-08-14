package bankoffers;

import java.time.LocalDate;

public class Offer {
    private final String bank;
    private final String vendor;
    private final double percent;
    private final double amount;
    private final double minimum; // minimum spent to avail
    private final double maximum; // maximum reward
    private final LocalDate expiration;

    public Offer(String bank, String vendor, double percent, double amount, double min, double max,
            LocalDate expiration) {
        this.bank = bank;
        this.vendor = vendor;
        this.percent = percent;
        this.amount = amount;
        this.minimum = min;
        this.maximum = max;
        this.expiration = expiration;
    }

    public Boolean hasExpired() {
        LocalDate currentDate = LocalDate.now();
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

    public double getPercent() {
        return percent;
    }

    public double getAmount() {
        return amount;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

}
