package kr.co.parkcom.store.api.datalab;

import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.util.ConfigMapReader;

public class DataLabContextServiceTest {

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        String datalabKey = config.get("datalabApiKey");
        String datalabValue = config.get("datalabApiPwd");
        String datalabUrl = config.get("datalabApiUrl");

        try {
            DataLabContextService service = new DataLabContextService(datalabKey , datalabValue , datalabUrl);
            KeywordMonth keywordMonth =  service.KeywordMonthTrend("우산");
            System.out.println(keywordMonth.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
