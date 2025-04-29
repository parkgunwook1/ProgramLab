package kr.co.parkcom.store.domain.keyword.service.gpt;

import java.io.IOException;

public interface IGptKeywordGenerator {

    public String extractKeywords(String topic, int count, String context) throws IOException, Exception;
}
