package kr.co.parkcom.store.domain.keyword.service.datalab;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLabKeywordService implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private GptKeywordListService gptKeywordListService;
    private DataLabContextService dataLabContextService;
    private IDBManager idbManager;
    public DataLabKeywordService(GptKeywordListService gptKeywordListService , DataLabContextService dataLabContextService , IDBManager idbManager) {
        this.gptKeywordListService = gptKeywordListService;
        this.dataLabContextService = dataLabContextService;
        this.idbManager = idbManager;

    }

    @Override
    public void run() {
         while (true) {
             try {
                 if (gptKeywordListService.getKeywordListSize() > 0) {
                     String keyword = gptKeywordListService.getKeyword();
//                     KeywordStatisticsTrendResult result = dataLabContextService.analyzeKeywordTrend(keyword);
//                     int dbResult = idbManager.insertKeywordStatisticsTrend(result);
                     KeywordMonth result = dataLabContextService.KeywordMonthTrend(keyword);
                     int dbResult = idbManager.insertKeywordMonth(result);

                     if (dbResult == 0) {
                         log.error("db insert failed");
                     }

                 }else {
                     System.out.println("gptKeyword가 0보다 작습니다..");
                 }

                 Thread.sleep(60_000);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }
}
