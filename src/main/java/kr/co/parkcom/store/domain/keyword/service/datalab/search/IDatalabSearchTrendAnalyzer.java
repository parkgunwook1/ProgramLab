package kr.co.parkcom.store.domain.keyword.service.datalab.search;

import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordStatisticsTrendResult;

public interface IDatalabSearchTrendAnalyzer {
    KeywordStatisticsTrendResult analyzeSearchKeywordTrend(String keyword) throws Exception;
    KeywordSearchMonth KeywordSearchMonthTrend(String keyword) throws Exception;
}
