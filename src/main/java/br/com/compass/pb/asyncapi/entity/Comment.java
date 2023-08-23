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

    @JsonIgnore
    private Long postId;
}
