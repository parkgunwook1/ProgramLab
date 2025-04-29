package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.dto.KeywordTrendResult;

public class MockDBManager implements IDBManager{


    @Override
    public int insertKeywordTrend(KeywordTrendResult keywordTrendResult) {
        System.out.println(keywordTrendResult.toString());
        return 0;
    }

    @Override
    public int selectKeywordTrend() {
        return 0;
    }
}
