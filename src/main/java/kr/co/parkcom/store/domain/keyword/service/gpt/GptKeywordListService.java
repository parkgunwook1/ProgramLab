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
//                    List<String> list = idbManager.selectKeywordTrend();
//                    GptResponse response = gptContextService.makeListDatalabKeyword(list);
//                    String content = response.getChoices().get(0).getMessage().getContent().trim();

                    String content = "캠핑의자, 텀블러, 선풍기, 모기퇴치기, 무선청소기, 블루투스스피커, 가습기, 전기담요, 가습기, 무선청소기, 블루투스스피커, 스마트워치, 노트북스탠드, 무드등, 차량용핸드폰거치대, 전동칫솔, 무선충전기, 게이밍마우스, 미니빔프로젝터, 전기요, 가습기, 전동드릴, 전기히터, 전동킥보드, 무선이어폰, 스마트폰삼각대, 전자책리더기, 전기포트, 공기청정기, 전동마사지기, 전기담요, 스마트플러그, 차량용청소기, 전기그릴, 캠핑의자, 캠핑테이블, 텐트, 낚시의자, 낚시릴, 자전거헬멧, 자전거라이트, 등산스틱, 등산배낭, 피크닉매트, 여행용캐리어, 여행용파우치, 여행용목베개, 여행용세면도구통, 여행용슬리퍼, 여행용압축팩, 여행용가방, 여행용보조배터리, 여행용멀티어댑터, 여행용세탁망, 여행용텀블러, 여행용우산, 여행용안대, 여행용귀마개, 여행용칫솔케이스, 서큘레이터, 제습기, 선글라스, 모기퇴치기, 방충망, 쿨매트, 쿨토시, 쿨스카프, 아이스팩, 휴대용선풍기, 물놀이튜브, 비치타월, 방수팩, 방수스피커, 방수백, 방수시계, 방수카메라, 방수모자, 방수신발, 방수이어폰, 방수지갑, 방수파우치, 방수의자, 방수테이블, 방수텐트, 온수매트, 전기장판, 난방텐트, 전기난로, 전기방석, 전기발난로, 전기손난로, 전기찜질기, 전기매트, 전기온풍기, 전기커튼, 전기벽난로, 전기난방기, 전기히터팬, 전기난방매트, 전기난방텐트, 전기난방의자, 전기난방테이블, 전기난방쿠션, 전기난방슬리퍼, 전기난방장갑, 전기난방담요, 전기난방베개, 전기난방매트리스, 전기난방카페트, 무선청소기, 블루투스스피커, 스마트워치, 무드등, 전동칫솔, 노트북스탠드, 차량용핸드폰거치대, 무선충전기, 게이밍마우스, 미니빔프로젝터, 전기요, 전기담요, 가습기, 공기청정기, 전동드릴, 전동스크류드라이버, 캠핑의자, 캠핑테이블, 텐트, 낚시릴, 낚시가방, 자전거라이트, 자전거헬멧, 러닝화, 요가매트, 스마트락, 전자도어락, 블라인드, 암막커튼, 무선이어폰, 태블릿거치대, 태블릿케이스, 스마트폰케이스, 강화유리필름, 셀카봉, 삼각대, 미니선풍기, 서큘레이터, 전기포트, 전기그릴, 전기프라이팬, 전기밥솥, 전기주전자, 전기토스터, 전기오븐, 믹서기, 커피머신, 에어프라이어, 전기다리미, 스팀다리미, 보조배터리, USB허브, 외장하드, SSD, 마우스패드, 키보드, 기계식키보드, 게이밍헤드셋, 웹캠, 모니터받침대, 모니터암, 프린터, 라벨프린터, 스탠드조명, 독서대, 책상정리함, 파일박스, 수납함, 옷걸이, 신발정리대, 진공청소기, 로봇청소기, 스팀청소기, 물걸레청소기, 청소기필터, 여행가방, 캐리어, 백팩, 크로스백, 에코백, 파우치, 지갑, 카드지갑, 명함지갑, 벨트, 모자, 선글라스, 안경테, 시계, 팔찌, 목걸이, 귀걸이";
                    String[] keywords = content.split(",");

                    for (String keyword : keywords) {
                        System.out.println("keyword : " + keyword);
                        synchronized (keywordList) {
                            keywordList.add(keyword.trim());
                        }
                    }
                    System.out.println("[GPT 공급] 키워드 리스트 채움. 현재 : " + getKeywordListSize() + "개");
                }

                Thread.sleep(60_000);
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
