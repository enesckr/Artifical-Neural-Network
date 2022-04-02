public class Normalization {

    private double dataHigh, dataLow, normalizedHigh, normalizedLow;

    public Normalization(double dataHigh, double dataLow, double normalizedHigh, double normalizedLow) {
        this.dataHigh = dataHigh;
        this.dataLow = dataLow;
        this.normalizedHigh = normalizedHigh;
        this.normalizedLow = normalizedLow;
    }

    public double normalize(double x) {
        return ((x - getDataLow())
                / (getDataHigh() - getDataLow()))
                * (getNormalizedHigh() - getNormalizedLow()) + getNormalizedLow();
    }

    public double denormalize(double y) {
        return (y - getNormalizedLow()) * (getDataHigh() - getDataLow()) / (getNormalizedHigh() - getNormalizedLow()) + getNormalizedLow();
    }

    public double getDataHigh() {
        return dataHigh;
    }

    public void setDataHigh(double dataHigh) {
        this.dataHigh = dataHigh;
    }

    public double getDataLow() {
        return dataLow;
    }

    public void setDataLow(double dataLow) {
        this.dataLow = dataLow;
    }

    public double getNormalizedHigh() {
        return normalizedHigh;
    }

    public void setNormalizedHigh(double normalizedHigh) {
        this.normalizedHigh = normalizedHigh;
    }

    public double getNormalizedLow() {
        return normalizedLow;
    }

    public void setNormalizedLow(double normalizedLow) {
        this.normalizedLow = normalizedLow;
    }

}
