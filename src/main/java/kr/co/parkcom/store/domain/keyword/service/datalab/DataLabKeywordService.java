package kr.co.parkcom.store.domain.keyword.service.datalab;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
        int dbResult = 0;
         while (true) {
             try {
                 if (gptKeywordListService.getKeywordListSize() > 0) {
                     String keyword = gptKeywordListService.getKeyword();
                     int count = 0;
                     List<String> dbList;
                     dbList = idbManager.selectMonthKeyword();

                     for (String list : dbList) {
                         if (list.equals(keyword)) {
                             count++;
                         }
                     }
                     if (count != 0) {
                         KeywordMonth result = dataLabContextService.KeywordMonthTrend(keyword);
                         idbManager.insertKeywordMonth(result);
                         dbResult++;
                     }
                 }
                 System.out.println(dbResult);
                 Thread.sleep(2_000);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }
}
