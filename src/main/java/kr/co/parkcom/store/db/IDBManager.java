package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;

import java.util.List;


public interface IDBManager {

    int insertKeywordStatisticsTrend(KeywordStatisticsTrendResult keywordTrendResult);
    void insertKeywordMonth(KeywordMonth keywordMonth);
    List<String> selectKeywordTrend();
    List<String> selectMonthKeyword();

}
