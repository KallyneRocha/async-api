package br.com.compass.pb.asyncapi.dto;

public class CommentDTO {
    private long id;
    private long postId;
    private String body;

    public CommentDTO(long id, long postId, String body) {
        this.id = id;
        this.postId = postId;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

