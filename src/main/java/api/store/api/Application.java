package api.store.api;

import api.store.datalab.DataLabContextServer;
import api.store.gpt.GptContextService;
import api.store.util.ConfigMapReader;

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

        System.out.println("gptApi key : " + gptApiKey);
        System.out.println("datalab key : " + datalabKey + "datalab pwd : "+ datalabValue);

        DataLabContextServer datalab = new DataLabContextServer(datalabKey , datalabValue);
        GptContextService gptServer = new GptContextService(gptApiKey,gptApiUrl,gptModel,gtpTemperature);
        
        String prompt = "배게와 관련된 좋은 키워드를 추천해줘";
        int count = 5;
        try {
            String result = gptServer.extractKeywords(prompt , count);
            System.out.println(result);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
