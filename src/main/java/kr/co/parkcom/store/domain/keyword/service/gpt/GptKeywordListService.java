package kr.co.parkcom.store.domain.keyword.service.gpt;

import kr.co.parkcom.store.api.gpt.GptContextService;
import kr.co.parkcom.store.api.gpt.dto.GptResponse;
import kr.co.parkcom.store.db.IDBManager;

import java.util.ArrayList;
import java.util.List;

public class GptKeywordListService implements Runnable {

    private List<String> searchList;
    private List<String> categoryList;
    private GptContextService gptContextService;
    private IDBManager idbManager;

    public GptKeywordListService(GptContextService gptContextService, IDBManager idbManager) {
        this.gptContextService = gptContextService;
        this.idbManager = idbManager;
        this.searchList = new ArrayList<>();
        this.categoryList = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (getSearchListSize() < 5) {
                    System.out.println("[GPT 공급] 검색량 키워드 부족. GPT 호출 ");

                    List<String> list = idbManager.selectKeywordTrend();
                    GptResponse response = gptContextService.makeSearchList(list);
                    String content = response.getChoices().get(0).getMessage().getContent().trim();

                    String[] keywords = content.split(",");

                    for (String keyword : keywords) {
                        System.out.println("keyword : " + keyword);
                        synchronized (searchList) {
                            searchList.add(keyword.trim());
                        }
                    }
                    System.out.println("[GPT 공급] 검색량 키워드 리스트 채움. 현재 : " + getSearchListSize() + "개");
                } else if (getClickListSize() < 5) {
                    System.out.println("[GPT 공급] 카테고리 키워드 부족. GPT 호출");

                    GptResponse response = gptContextService.makeClickList();
                    String content = response.getChoices().get(0).getMessage().getContent().trim();

                    String[] keywords = content.split(",");

                    for (String keyword : keywords) {
                        System.out.println("keyword : " + keyword);
                        synchronized (searchList) {
                            searchList.add(keyword.trim());
                        }
                    }
                    System.out.println("[GPT 공급] 카테고리 키워드 리스트 채움. 현재 : " + getSearchListSize() + "개");
                }

                Thread.sleep(2_000);
            } catch (Exception e) {
                System.out.println("gpt select thread error ");
                e.printStackTrace();
            }
        }
    }

    public int getClickListSize() {
        int size;
        synchronized (categoryList) {
            size = categoryList.size();
        }
        return size;
    }

    public int getSearchListSize() {
        int size;
        synchronized (searchList) {
            size = searchList.size();
        }
        return size;
    }

    public String getKeyword() {
        synchronized (searchList) {
            if (!searchList.isEmpty()) {
                return searchList.remove(0);
            }
        }
        return null;
    }
}
