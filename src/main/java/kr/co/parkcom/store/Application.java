package kr.co.parkcom.store;


import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.api.gpt.GptContextService;
import kr.co.parkcom.store.db.DBManager;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.db.MockDBManager;
import kr.co.parkcom.store.domain.keyword.service.datalab.DataLabKeywordService;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import kr.co.parkcom.store.util.ConfigMapReader;


public class Application {

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        String gptApiKey = config.get("gptApiKey");
        String gptApiUrl = config.get("gptApiUrl");
        String gptModel = config.get("gptModel");
        double gtpTemperature = Double.parseDouble(config.get("gptTemperature"));

        String datalabKey = config.get("datalabApiKey");
        String datalabValue = config.get("datalabApiPwd");
        String datalabUrl = config.get("datalabApiUrl");


        IDBManager idbManager;
        String dbType = config.get("dbType");

        if (dbType.equals("Y")) {
            idbManager = new DBManager();
        }else {
            idbManager = new MockDBManager();
        }
        GptContextService gptService = null;
        DataLabContextService datalabService = null;


        try {
            gptService = new GptContextService(gptApiKey, gptApiUrl, gptModel, gtpTemperature);
            datalabService = new DataLabContextService(datalabKey, datalabValue, datalabUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GptKeywordListService keywordListService = new GptKeywordListService(gptService);
        Thread gptThread = new Thread(keywordListService);
        gptThread.start();

        DataLabKeywordService dataLabKeywordService = new DataLabKeywordService(keywordListService , datalabService, idbManager);
        Thread datalabThread = new Thread(dataLabKeywordService);
        datalabThread.start();

    }
}
