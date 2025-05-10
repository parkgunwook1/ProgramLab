package kr.co.parkcom.store.api.gpt.dto;

import java.util.List;

public class GptResponse {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private int index;
        private Message message;
        public Message getMessage() { return message; }
        public void setMessage(Message message) { this.message = message; }
    }

    public static class Message {
        private String role;
        private String content;
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
