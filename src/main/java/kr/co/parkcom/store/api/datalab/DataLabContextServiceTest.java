package kr.co.parkcom.store.api.datalab;

import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.util.ConfigMapReader;

public class DataLabContextServiceTest {

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        String datalabKey = config.get("datalabApiKey");
        String datalabValue = config.get("datalabApiPwd");
//        String datalabUrl = config.get("datalabApiUrl");

        String datalabClickUrl = config.get("datalabApiClickUrl");
        try {
            DataLabContextService service = new DataLabContextService(datalabKey , datalabValue , datalabClickUrl);
//            KeywordSearchMonth keywordMonth =  service.KeywordSearchMonthTrend("우산");

//            service.KeywordClickTrend();
//                System.out.println(keywordMonth.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
