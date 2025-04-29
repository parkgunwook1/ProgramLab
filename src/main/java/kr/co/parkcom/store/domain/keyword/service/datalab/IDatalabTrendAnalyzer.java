package kr.co.parkcom.store.domain.keyword.service.datalab;

import kr.co.parkcom.store.domain.keyword.dto.KeywordTrendResult;

public interface IDatalabTrendAnalyzer {
    KeywordTrendResult analyzeKeywordTrend(String keyword) throws Exception;
}
