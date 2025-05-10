package kr.co.parkcom.store.db;

import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordStatisticsTrendResult;

import java.util.List;


public interface IDBManager {

    int insertKeywordStatisticsTrend(KeywordStatisticsTrendResult keywordTrendResult);
    void insertKeywordMonth(KeywordSearchMonth keywordMonth);
    void insertCategoryList(KeywordSearchMonth gptClickResponse);
    List<String> selectKeywordTrend();
    List<String> selectMonthKeyword();

}
