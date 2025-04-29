package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.dto.KeywordTrendResult;

public interface IDBManager {

    int insertKeywordTrend(KeywordTrendResult keywordTrendResult);
    int selectKeywordTrend();

}
