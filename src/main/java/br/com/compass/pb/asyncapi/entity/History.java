package br.com.compass.pb.asyncapi.entity;

import br.com.compass.pb.asyncapi.enums.PostState;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    private PostState status;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public History() {
            this.date = LocalDateTime.now();
    }

    public History(PostState status, Post post) {
            this.date = LocalDateTime.now();
            this.status = status;
            this.post = post;
    }

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    public LocalDateTime getDate() {
            return date;
    }

    public void setDate(LocalDateTime date) {
            this.date = date;
    }

    public PostState getStatus() {
            return status;
    }

    public void setStatus(PostState status) {
            this.status = status;
    }

    public Post getPost() {
            return post;
    }

    public void setPost(Post post) {
            this.post = post;
    }

}
