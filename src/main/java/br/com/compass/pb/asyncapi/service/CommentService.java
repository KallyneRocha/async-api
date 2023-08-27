package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.client.ApiConsumerFeign;
import br.com.compass.pb.asyncapi.dto.CommentDTO;
import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final ApiConsumerFeign apiConsumerFeign;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(ApiConsumerFeign apiConsumerFeign, CommentRepository commentRepository) {
        this.apiConsumerFeign = apiConsumerFeign;
        this.commentRepository = commentRepository;
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return apiConsumerFeign.getCommentByPostId(postId);
    }

    public void saveComments(List<CommentDTO> comments, Post post) {
        for (CommentDTO commentDTO : comments) {
            Comment comment = new Comment(commentDTO.getId(), post, commentDTO.getBody());
            commentRepository.save(comment);
        }
    }

}
