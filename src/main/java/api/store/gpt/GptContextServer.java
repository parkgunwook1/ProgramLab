package api.store.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GptContextServer {

    private String API_KEY;
    private String API_URL;
    private Gson gson;
    private final OkHttpClient client;


    public GptContextServer(String key, String apiUrl) {
        this.API_KEY = key;
        this.API_URL = apiUrl;
        this.gson = new Gson();

        client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS) // 연결 타임아웃
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS) // 쓰기 타임아웃
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)  // 읽기 타임아웃
                .build();
    }

    public String makeSentence(String keyName, int count) {

        return "";
    }

    public String makeSentence(String keyName, int count, String sentence) {

        return "";
    }

    public String sendPrompt(String prompt) throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4.1");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", prompt));

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);

            String responseBody = response.body().string();
            Map<?, ?> map = mapper.readValue(responseBody, Map.class);
            List<?> choices = (List<?>) map.get("choices");
            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");

            return message.get("content").toString().trim();
        }
    }
}