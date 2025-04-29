package kr.co.parkcom.store.domain.keyword.service.datalab;

import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.domain.keyword.dto.KeywordTrendResult;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;

public class DataLabKeywordService implements Runnable{

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
                     KeywordTrendResult result = dataLabContextService.analyzeKeywordTrend(keyword);
                     idbManager.insertKeywordTrend(result);

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
