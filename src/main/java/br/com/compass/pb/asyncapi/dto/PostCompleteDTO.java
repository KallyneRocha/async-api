package br.com.compass.pb.asyncapi.dto;

import java.util.List;

public class PostCompleteDTO {
    private long id;
    private String title;
    private String body;
    private List<CommentDTO> comments;
    private List<HistoryDTO> history;

    public PostCompleteDTO() {
    }

    public PostCompleteDTO(long id, String title, String body, List<CommentDTO> comments, List<HistoryDTO> history) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.comments = comments;
        this.history = history;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<HistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDTO> history) {
        this.history = history;
    }
}
