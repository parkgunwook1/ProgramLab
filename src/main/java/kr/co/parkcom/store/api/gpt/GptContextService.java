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

    public GptResponse makeListDatalabKeyword(List<String> list) throws IOException , Exception {

        String excludeList = String.join(", ", list);
        System.out.println("make gpt - exclude Keyword list : " + excludeList);

        String prompt = "다음 조건에 맞춰 키워드를 생성해줘.\n\n" +
                "조건:\n" +
                "- 쿠팡, 스마트스토어, 지마켓, 11번가, 옥션 같은 **국내 온라인 마켓에서 실제로 판매 가능한 상품 키워드**만 생성할 것\n" +
                "- **음식/식품은 제외**하고, 실체가 있는 물건(헬스케어 , 강아지 , 가전 , 인테리어 , 스포츠, 디지털 등)만 포함\n" +
                "- **2024년 1월~12월까지의 검색 트렌드를 기반으로**, 실제로 많이 검색되고 판매 가능성이 높은 키워드 위주로\n" +
                "- 키워드는 GPT 네가 봐도 '**내가 소비자라면 이건 사야 한다**'고 느껴질 만한 상품 위주로 선정할 것\n" +
                "- 총 600개 키워드를 월별 50개씩 구성 (월별은 1월부터 12월까지)\n" +
                "- 키워드는 **콤마(,)로 구분된 하나의 문자열**로만 반환하고, 설명은 절대 붙이지 말 것\n" +
                "- 콤마 뒤에는 반드시 한 칸 띄어 쓸 것\n" +
                "추가 조건:\n" +
                "- **:\n";

        GptResponse response = sendPrompt(prompt);
        // "- 분기에 25개씩 총 100개 키워드를 뽑아줘\n" +

        return response;
    }
}