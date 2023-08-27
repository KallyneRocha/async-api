package br.com.compass.pb.asyncapi.dto;

import br.com.compass.pb.asyncapi.enums.PostState;

import java.time.LocalDateTime;

public class HistoryDTO {
    private Long id;
    private Long postId;
    private LocalDateTime date;
    private PostState state;

    public HistoryDTO() {
    }

    public HistoryDTO(Long id, Long postId, LocalDateTime date, PostState state) {
        this.id = id;
        this.postId = postId;
        this.date = date;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PostState getState() {
        return state;
    }

    public void setState(PostState state) {
        this.state = state;
    }
}

