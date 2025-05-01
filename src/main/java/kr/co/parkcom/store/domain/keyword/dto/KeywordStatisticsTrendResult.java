package kr.co.parkcom.store.domain.keyword.dto;

public class KeywordStatisticsTrendResult {

    private String keyword;
    private int q1Avg;
    private int q2Avg;
    private int q3Avg;
    private int q4Avg;

    public KeywordStatisticsTrendResult(String keyword, int q1Avg, int q2Avg, int q3Avg, int q4Avg) {
        this.keyword = keyword;
        this.q1Avg = q1Avg;
        this.q2Avg = q2Avg;
        this.q3Avg = q3Avg;
        this.q4Avg = q4Avg;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getQ1Avg() {
        return q1Avg;
    }

    public int getQ2Avg() {
        return q2Avg;
    }

    public int getQ3Avg() {
        return q3Avg;
    }


    public int getQ4Avg() {
        return q4Avg;
    }

    @Override
    public String toString() {
        return "KeywordTrendResult{" +
                "keyword='" + keyword + '\'' +
                ", q1Avg=" + q1Avg +
                ", q2Avg=" + q2Avg +
                ", q3Avg=" + q3Avg +
                ", q4Avg=" + q4Avg +
                '}';
    }


}
