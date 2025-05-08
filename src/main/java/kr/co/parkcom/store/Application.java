package kr.co.parkcom.store;


import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.api.gpt.GptContextService;
import kr.co.parkcom.store.db.DBManager;
import kr.co.parkcom.store.db.IDBManager;
import kr.co.parkcom.store.db.MockDBManager;
import kr.co.parkcom.store.domain.keyword.service.datalab.search.DataLabKeywordSearchService;
import kr.co.parkcom.store.domain.keyword.service.gpt.GptKeywordListService;
import kr.co.parkcom.store.util.ConfigMapReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

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


        IDBManager idbManager = null;
        String dbType = config.get("dbType");

        String dbUrl = config.get("db.url");
        String dbUser = config.get("db.user");
        String dbPwd = config.get("db.password");

        Connection dbCon = null;

        try  {
            dbCon = DriverManager.getConnection(dbUrl , dbUser , dbPwd);
            if (dbCon != null) {
                log.info("db connection success");
                if (dbType.equals("Y")) {
                    idbManager = new DBManager(dbCon);
                }else {
                    idbManager = new MockDBManager();
                }
            }
        }catch (SQLException e) {
            log.error("connection : {} " ,e.getMessage());
        }

        GptContextService gptService = null;
        DataLabContextService datalabService = null;

        try {
            gptService = new GptContextService(gptApiKey, gptApiUrl, gptModel, gtpTemperature);
            datalabService = new DataLabContextService(datalabKey, datalabValue, datalabUrl);
        } catch (Exception e) {
            log.error(e.getMessage() , e);
        }

        GptKeywordListService keywordListService = new GptKeywordListService(gptService , idbManager);
        Thread gptThread = new Thread(keywordListService);
        gptThread.start();

        DataLabKeywordSearchService dataLabKeywordService = new DataLabKeywordSearchService(keywordListService , datalabService, idbManager);
        Thread datalabThread = new Thread(dataLabKeywordService);
        datalabThread.start();

    }
}
