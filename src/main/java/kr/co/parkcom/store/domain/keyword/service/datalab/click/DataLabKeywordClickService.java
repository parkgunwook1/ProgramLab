package kr.co.parkcom.store.domain.keyword.service.datalab.click;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        while (true){
            try {
                if (gptKeywordListService.getKeywordListSize() > 0) {
                    String keyword = gptKeywordListService.getKeyword();


                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
