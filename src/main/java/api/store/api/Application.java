package api.store.api;

import api.store.datalab.DataLabContextServer;
import api.store.gpt.GptContextServer;
import api.store.util.ConfigMapReader;

public class Application {

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        String gptApiKey = config.get("gptApiKey");
        String gptApiUrl = config.get("gptApiUrl");

        String datalabKey = config.get("datalabApiKey");
        String datalabValue = config.get("datalabApiPwd");

        System.out.println("gptApi key : " + gptApiKey);
        System.out.println("datalab key : " + datalabKey + "datalab pwd : "+ datalabValue);

        DataLabContextServer datalab = new DataLabContextServer(datalabKey , datalabValue);
        GptContextServer gptServer = new GptContextServer(gptApiKey,gptApiUrl);
        
        String prompt = "배게와 관련된 좋은 키워드를 추천해줘";
        try {
            gptServer.sendPrompt(prompt);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
