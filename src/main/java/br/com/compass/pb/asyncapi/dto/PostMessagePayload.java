package br.com.compass.pb.asyncapi.dto;

public class PostMessagePayload {

    private Long postId;
    private String operationType;

    public PostMessagePayload(Long postId, String operationType) {
        this.postId = postId;
        this.operationType = operationType;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}

