package kr.co.parkcom.store;

import kr.co.parkcom.store.api.datalab.DataLabContextService;
import kr.co.parkcom.store.util.ConfigMapReader;

public class SearchTest {

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        String gptApiKey = config.get("gptApiKey");
        String gptApiUrl = config.get("gptApiUrl");
        String gptModel = config.get("gptModel");
        double gtpTemperature = Double.parseDouble(config.get("gptTemperature"));

        String datalabKey = config.get("datalabApiKey");
        String datalabValue = config.get("datalabApiPwd");
        String datalabSearchUrl = config.get("datalabApiUrl");
        String datalabClickUrl = config.get("datalabApiClickUrl");

        try {
        DataLabContextService searchService = new DataLabContextService(datalabKey , datalabValue , datalabSearchUrl);
        DataLabContextService clickService = new DataLabContextService(datalabKey , datalabValue , datalabClickUrl);

        clickService.KeywordClickTrend();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}
