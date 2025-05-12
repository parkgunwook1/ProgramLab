package kr.co.parkcom.store.domain.keyword.service.datalab.click;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLabKeywordClickService implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private GptKeywordListService gptKeywordListService;
    private DataLabContextService dataLabContextService;
    private IDBManager idbManager;
    public DataLabKeywordClickService(GptKeywordListService gptKeywordListService , DataLabContextService dataLabContextService , IDBManager idbManager) {
        this.gptKeywordListService = gptKeywordListService;
        this.dataLabContextService = dataLabContextService;
        this.idbManager = idbManager;

    }
    @Override
    public void run() {
        int dbResult = 0;
        while (true){
            try {
                if (gptKeywordListService.getClickListSize() > 0) {
                    String keyword = gptKeywordListService.getClickKeyword();

                    KeywordSearchMonth result = dataLabContextService.KeywordClickTrend(keyword);
                    System.out.println(result.toString());
                    idbManager.insertCategoryList(result);
                    dbResult++;
                }
                System.out.println(dbResult);
                Thread.sleep(2_000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
