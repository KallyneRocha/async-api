package br.com.compass.pb.asyncapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    private Long id;

    @Column
    private String body;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;

    public Comment() {
    }

    public Comment(Long id, Post post, String body) {
        this.id = id;
        this.body = body;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
