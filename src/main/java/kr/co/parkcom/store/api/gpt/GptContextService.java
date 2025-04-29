package kr.co.parkcom.store.api.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.parkcom.store.api.gpt.dto.GptResponse;
import kr.co.parkcom.store.domain.keyword.service.gpt.IGptKeywordGenerator;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * GPT API 호출과 키워드 추출 로직을 책임지는 서비스 클래스
 */
public class GptContextService implements IGptKeywordGenerator {

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final String apiKey;
    private final String apiUrl;
    private final String model;
    private final double temperature;

    public GptContextService(String apiKey, String apiUrl, String model, double temperature) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.mapper = new ObjectMapper();
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
        this.temperature = temperature;
    }

    /**
     * 단어만으로 핵심 키워드 추출
     */
    public String extractKeywords(String topic, int count) throws IOException, Exception {
        return extractKeywords(topic, count, null);
    }

    /**
     * 단어와 문맥를 함께 참고하여 핵심 키워드 추출
     */
    @Override
    public String extractKeywords(String topic, int count, String context) throws IOException, Exception {
        String prompt = buildPrompt(topic, count, context);
        GptResponse resp = sendPrompt(prompt);
        return resp.getChoices().stream()
                .findFirst()
                .map(c -> c.getMessage().getContent().trim())
                .orElseThrow(() -> new RuntimeException("choices가 없습니다."));
    }

    /**
     * GPT에 보낼 프롬프트 생성
     */
    private String buildPrompt(String topic, int count, String context) {
        if (context == null || context.isBlank()) {
            return String.format(
                    "마켓에 판매할건데 핵심 키워드를 뽑을거야. 조건은 다음 단어 \"%s\"와 관련된 핵심 키워드 %d개를 콤마(,)로 구분된 형태로 뽑아서 알려줘.",
                    topic, count);
        }
        return String.format(
                "마켓에 판매할건데 핵심 키워드를 뽑을거야. 조건은 아래 문장을 참고해서, 주제 \"%s\"와 관련된 핵심 키워드 %d개를 콤마(,)로 구분된 형태로 뽑아줘.\n\n문장: %s",
                topic, count, context);
    }

    private GptResponse sendPrompt(String prompt) throws IOException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4.1");    // gpt version 기입
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("temperature", 0.0);

        String json = mapper.writeValueAsString(requestBody);
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }
            String respJson = response.body().string();
            return mapper.readValue(respJson, GptResponse.class);
        }
    }

    /**
     * datalab에 사용할 list  추출
     */

    public GptResponse makeListDatalabKeyword() throws IOException , Exception {
        String prompt = "다음 조건에 맞춰 데이터를 만들어줘.\n" +
                "\n" +
                "조건:\n" +
                "- 마켓에 판매할 수 있는 **물건** 키워드만 (음식 제외)\n" +
                "- 1월부터 12월까지 데이터 기반으로 많이 검색 되고 **잘 팔릴 것 같은 상품**을 중심으로\n" +
                "- 분기에 2개씩 총 8개 키워드를 뽑아줘\n" +
                "- 키워드는 **콤마(,)로 구분된 하나의 문자열**로 반환해줘\n" +
                "- 예시: 신발, 슬리퍼, 선풍기, 물병, 이어폰, ...\n" +
                "\n" +
                "추가 지시:\n" +
                "- 결과는 오직 키워드만 나열하고 설명은 하지 말 것\n" +
                "- 콤마 뒤에는 반드시 **한 칸 띄워서** 키워드를 적을 것";

        GptResponse response = sendPrompt(prompt);
        // "- 분기에 25개씩 총 100개 키워드를 뽑아줘\n" +

        return response;
    }
}