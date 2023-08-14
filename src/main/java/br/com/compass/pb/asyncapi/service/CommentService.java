package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void saveComments(List<Comment> comments, Long postId) {
    }
}
