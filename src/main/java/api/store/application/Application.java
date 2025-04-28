package api.store.application;

import api.store.datalab.NaverDataLabClient;
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
        String datalabUrl = config.get("datalabApiUrl");

        GptContextService gptServer = new GptContextService(gptApiKey, gptApiUrl, gptModel, gtpTemperature);

        String prompt = "배게와 관련된 좋은 키워드를 추천해줘";
        int count = 5;
        try {
            NaverDataLabClient datalab = new NaverDataLabClient(datalabKey, datalabValue, datalabUrl);
            datalab.requestDatalabApi();
//            String result = gptServer.extractKeywords(prompt, count);
//            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
