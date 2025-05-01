package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;

import java.util.List;


public class MockDBManager implements IDBManager{


    @Override
    public int insertKeywordStatisticsTrend(KeywordStatisticsTrendResult keywordTrendResult) {
        System.out.println(keywordTrendResult.toString());
        return 0;
    }

    @Override
    public int insertKeywordMonth(KeywordMonth keywordMonth) {
        return 0;
    }

    @Override
    public List<String> selectKeywordTrend() {
        return null;
    }
}
