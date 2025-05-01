package kr.co.parkcom.store.domain.keyword.service.datalab;

import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;

public interface IDatalabTrendAnalyzer {
    KeywordStatisticsTrendResult analyzeKeywordTrend(String keyword) throws Exception;
    KeywordMonth KeywordMonthTrend(String keyword) throws Exception;
}
