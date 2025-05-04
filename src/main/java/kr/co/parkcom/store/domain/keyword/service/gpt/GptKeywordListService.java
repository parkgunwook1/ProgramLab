package kr.co.parkcom.store.domain.keyword.service.gpt;

import kr.co.parkcom.store.api.gpt.GptContextService;
import kr.co.parkcom.store.api.gpt.dto.GptResponse;
import kr.co.parkcom.store.db.IDBManager;

import java.util.ArrayList;
import java.util.List;

public class GptKeywordListService implements Runnable {

    private List<String> keywordList;
    private GptContextService gptContextService;
    private IDBManager idbManager;

    public GptKeywordListService(GptContextService gptContextService, IDBManager idbManager) {
        this.gptContextService = gptContextService;
        this.idbManager = idbManager;
        this.keywordList = new ArrayList<>();
    }
    @Override
    public void run() {
        while (true) {
            try {
                if (getKeywordListSize() < 5) {
                    System.out.println("[GPT 공급] 키워드 부족. GPT 호출 ");

                    // gpt  로직 빼도될듯?
                    //
                    List<String> list = idbManager.selectKeywordTrend();
                    GptResponse response = gptContextService.makeListDatalabKeyword(list);
                    String content = response.getChoices().get(0).getMessage().getContent().trim();

                    String[] keywords = content.split(",");

                    for (String keyword : keywords) {
                        System.out.println("keyword : " + keyword);
                        synchronized (keywordList) {
                            keywordList.add(keyword.trim());
                        }
                    }
                    System.out.println("[GPT 공급] 키워드 리스트 채움. 현재 : " + getKeywordListSize() + "개");
                }

                Thread.sleep(2_000);
            } catch (Exception e) {
                System.out.println("gpt select thread error ");
                e.printStackTrace();
            }
        }
    }

    public int getKeywordListSize() {
        int size;
        synchronized (keywordList) {
            size = keywordList.size();
        }
        return size;
    }

    public String getKeyword() {
        synchronized (keywordList) {
            if (!keywordList.isEmpty()) {
                return keywordList.remove(0);
            }
        }
        return null;
    }
}
