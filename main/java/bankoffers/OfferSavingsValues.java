package bankoffers;

// The purpose of this class is to pass values from the parser methods to the bank scraper
public class OfferSavingsValues {
    private final double percent;
    private final double amount;
    private final double minimum;
    private final double maximum;

    public OfferSavingsValues(double percent, double amount, double min, double max) {
        this.percent = percent;
        this.amount = amount;
        this.minimum = min;
        this.maximum = max;
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

}
