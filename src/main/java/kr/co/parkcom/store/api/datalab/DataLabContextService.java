package kr.co.parkcom.store.api.datalab;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.parkcom.store.api.datalab.dto.Data;
import kr.co.parkcom.store.api.datalab.dto.DataLabResponse;
import kr.co.parkcom.store.api.datalab.dto.Result;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordSearchMonth;
import kr.co.parkcom.store.domain.keyword.service.datalab.dto.KeywordStatisticsTrendResult;
import kr.co.parkcom.store.domain.keyword.service.datalab.search.IDatalabSearchTrendAnalyzer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataLabContextService implements IDatalabSearchTrendAnalyzer {

    private String apiId;
    private String apiPwd;
    private String apiUrl;
    private ObjectMapper mapper;


    public DataLabContextService(String apiId, String apiPwd, String apiUrl) throws Exception {
        this.apiId = apiId;
        this.apiPwd = apiPwd;
        this.apiUrl = apiUrl;
        this.mapper = new ObjectMapper();
    }

    private HttpURLConnection initConnection() throws Exception{
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        basicSettings(conn);

        return conn;
    }

    private void basicSettings(HttpURLConnection conn) throws Exception {
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-Naver-Client-Id", apiId);
        conn.setRequestProperty("X-Naver-Client-Secret", apiPwd);
        conn.setDoOutput(true);
    }


    /** category 테스트
     *
     */
    private static String ClickRequestBody() {
        return """
                            {
                               "startDate": "2024-01-01",
                               "endDate": "2024-12-31",
                               "timeUnit": "month",
                               "category": [
                                 {
                                   "name": "패션의류",
                                   "param": [ "50000000" ]
                                 },
                                 {
                                   "name": "화장품/미용",
                                   "param": [ "50000002" ]
                                 }
                               ],
                               "device": "pc",
                               "gender": "f",
                               "ages": [ "20", "30" ]
                             }
                }""";
    };

    /** 검색량 조회 메서드
     * startDate : 조회 시작일
     * endDate   : 조회 종료일
     * timeUnit  : 시간 단위
     * keywordGroups : 조회할 키워드 그룹 목록
     * device    : 조회 디바이스 ("pc" | "mo" | 생략 가능)
     * ages      : 조회할 연령대 코드 (3: 19~24세, 4: 25~29세, 5: 30~34세, 6: 35~39세, 7: 40~44세, 8: 45~49세, 9: 50~54세, 10: 55~59세)
     * gender    : 조회할 성별 ("m" | "f" | 생략 가능)
     */
    private static String SearchRequestBody(String keyword) {
        return """
                {
                  "startDate": "2024-01-01",
                  "endDate": "2024-12-30",
                  "timeUnit": "month",
                  "keywordGroups": [
                    {
                      "groupName": "테스트 그룹",
                      "keywords": ["%s"]
                    }
                  ],
                  "device": "pc",
                  "ages": ["3", "4", "5", "6", "7", "8", "9", "10"]
                }
                """.formatted(keyword);
    }

    /**
     * 분기별(Q1~Q4) 검색량 평균을 구하는 메서드
     */
    private KeywordStatisticsTrendResult analyzeQuarterlyStatistics(DataLabResponse response , String keyword) {
        Result result = response.getResults().get(0);
        List<Data> dataList = result.getData();

        double q1Sum = 0, q2Sum = 0, q3Sum = 0, q4Sum = 0;
        int q1Count = 0, q2Count = 0, q3Count = 0, q4Count = 0;

        for (Data data : dataList) {
            String period = data.getPeriod(); // "2024-01-01"
            int month = Integer.parseInt(period.substring(5, 7)); // "01" → 1

            // ratio : 	구간별 검색량의 상대적 비율. 구간별 결과에서 가장 큰 값을 100으로 설정한 상댓값
            if (month >= 1 && month <= 3) {
                q1Sum += data.getRatio();
                q1Count++;
            } else if (month >= 4 && month <= 6) {
                q2Sum += data.getRatio();
                q2Count++;
            } else if (month >= 7 && month <= 9) {
                q3Sum += data.getRatio();
                q3Count++;
            } else if (month >= 10 && month <= 12) {
                q4Sum += data.getRatio();
                q4Count++;
            }
        }

        int q1Avg = q1Count > 0 ? (int) (q1Sum / q1Count) : 0;
        int q2Avg = q2Count > 0 ? (int) (q2Sum / q2Count) : 0;
        int q3Avg = q3Count > 0 ? (int) (q3Sum / q3Count) : 0;
        int q4Avg = q4Count > 0 ? (int) (q4Sum / q4Count) : 0;

        KeywordStatisticsTrendResult keywordTrendResult = new KeywordStatisticsTrendResult(keyword , q1Avg , q2Avg , q3Avg , q4Avg);

        System.out.println("1분기(1~3월) 평균 검색 비율: " + q1Avg);
        System.out.println("2분기(4~6월) 평균 검색 비율: " + q2Avg);
        System.out.println("3분기(7~9월) 평균 검색 비율: " + q3Avg);
        System.out.println("4분기(10~12월) 평균 검색 비율: " + q4Avg);

        return keywordTrendResult;
    }

    /** 분기 검색량 통계 메서드
     * keyword mapping return -> "KeywordStatisticsTrendResult"
     */
    @Override
    public KeywordStatisticsTrendResult analyzeSearchKeywordTrend(String keyword) throws Exception {

        HttpURLConnection conn = initConnection();

        KeywordStatisticsTrendResult keywordTrendResult;
        String requestBody = SearchRequestBody(keyword);

        OutputStream os = conn.getOutputStream();
        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("응답결과 : ");
            System.out.println(response);

            DataLabResponse dataLabResponse = mapper.readValue(response, DataLabResponse.class);
            keywordTrendResult = analyzeQuarterlyStatistics(dataLabResponse, keyword);

        } else {
            String errorResponse = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new RuntimeException("API 호출 실패: " + responseCode + "\n" + errorResponse);

        }
            return keywordTrendResult;
    }

    /** 월별 검색량 메서드
     * keyword mapping return -> "KeywordStatisticsTrendResult"
     */
    @Override
    public KeywordSearchMonth KeywordSearchMonthTrend(String keyword) throws Exception {

        HttpURLConnection conn = initConnection();

        KeywordSearchMonth keywordMonthResult;
        String requestBody = SearchRequestBody(keyword);

        OutputStream os = conn.getOutputStream();
        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("응답결과 : ");
            System.out.println(response);

            DataLabResponse dataLabResponse = mapper.readValue(response, DataLabResponse.class);
            keywordMonthResult = analyzeKeywordMonthly(dataLabResponse, keyword);

        } else {
            String errorResponse = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new RuntimeException("API 호출 실패: " + responseCode + "\n" + errorResponse);

        }
        return keywordMonthResult;
    }

    /** 검색량 1~12월 구하는 메서드
     */
    private KeywordSearchMonth analyzeKeywordMonthly(DataLabResponse response , String keyword) {
        Result result = response.getResults().get(0);
        List<Data> dataList = result.getData();
        KeywordSearchMonth keywordMonth = new KeywordSearchMonth(keyword);

        for (Data data : dataList) {
            String period = data.getPeriod(); // "2024-01-01"
            int month = Integer.parseInt(period.substring(5, 7)); // "01" → 1

            // ratio : 	구간별 검색량의 상대적 비율. 구간별 결과에서 가장 큰 값을 100으로 설정한 상댓값
            switch (month) {
                case 1 -> keywordMonth.setMonth1((int) data.getRatio());
                case 2 -> keywordMonth.setMonth2((int) data.getRatio());
                case 3 -> keywordMonth.setMonth3((int) data.getRatio());
                case 4 -> keywordMonth.setMonth4((int) data.getRatio());
                case 5 -> keywordMonth.setMonth5((int) data.getRatio());
                case 6 -> keywordMonth.setMonth6((int) data.getRatio());
                case 7 -> keywordMonth.setMonth7((int) data.getRatio());
                case 8 -> keywordMonth.setMonth8((int) data.getRatio());
                case 9 -> keywordMonth.setMonth9((int) data.getRatio());
                case 10 -> keywordMonth.setMonth10((int) data.getRatio());
                case 11 -> keywordMonth.setMonth11((int) data.getRatio());
                case 12 -> keywordMonth.setMonth12((int) data.getRatio());
            }
        }
        return keywordMonth;
    }

    public void KeywordClickTrend() throws Exception{
        HttpURLConnection conn = initConnection();

        String requestBody = ClickRequestBody();


        OutputStream os = conn.getOutputStream();
        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("응답결과 : ");
            System.out.println(response);

//            DataLabResponse dataLabResponse = mapper.readValue(response, DataLabResponse.class);

        } else {
            String errorResponse = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new RuntimeException("API 호출 실패: " + responseCode + "\n" + errorResponse);

        }
    }
    }
