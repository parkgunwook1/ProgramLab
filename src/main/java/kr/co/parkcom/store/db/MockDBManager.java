package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordStatisticsTrendResult;

import java.util.List;


public class MockDBManager implements IDBManager{


    @Override
    public int insertKeywordStatisticsTrend(KeywordStatisticsTrendResult keywordTrendResult) {
        System.out.println(keywordTrendResult.toString());
        return 0;
    }

    @Override
    public void insertKeywordMonth(KeywordSearchMonth keywordMonth) {

    }

    @Override
    public List<String> selectKeywordTrend() {
        return null;
    }

    @Override
    public List<String> selectMonthKeyword() {
        return null;
    }
}
