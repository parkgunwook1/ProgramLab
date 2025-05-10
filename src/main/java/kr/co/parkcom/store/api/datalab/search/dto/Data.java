package kr.co.parkcom.store.api.datalab.search.dto;

public class Data {
    private String period; // ex) "2024-01-01"
    private double ratio;  // ex) 83.51

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}