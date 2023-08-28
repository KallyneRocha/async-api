package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.client.ApiConsumerFeign;
import br.com.compass.pb.asyncapi.dto.CommentDTO;
import br.com.compass.pb.asyncapi.dto.HistoryDTO;
import br.com.compass.pb.asyncapi.dto.PostCompleteDTO;
import br.com.compass.pb.asyncapi.dto.PostDTO;
import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import br.com.compass.pb.asyncapi.exception.InvalidPostStateException;
import br.com.compass.pb.asyncapi.queues.MessageProducer;
import br.com.compass.pb.asyncapi.repository.CommentRepository;
import br.com.compass.pb.asyncapi.repository.HistoryRepository;
import br.com.compass.pb.asyncapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final ApiConsumerFeign apiConsumerFeign;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;
    private final MessageProducer messageProducer;

    @Autowired
    public PostService(ApiConsumerFeign apiConsumerFeign, PostRepository postRepository, CommentRepository commentRepository, CommentService commentService,
                       HistoryRepository historyRepository, HistoryService historyService, MessageProducer messageProducer) {
        this.apiConsumerFeign = apiConsumerFeign;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.historyRepository = historyRepository;
        this.historyService = historyService;
        this.messageProducer = messageProducer;
    }

    //PROCESS - POST
    public void createPost(Long postId){
        validatePostId(postId);
        validatePostIsNotDuplicated(postId);
        Post post = new Post(postId, "","");
        postRepository.save(post);
        // CREATED
        historyService.addHistoryEntry(postId, PostState.CREATED);
        messageProducer.sendMessage("POST_FIND", postId);
    }
    @Async
    public void findPost(Long postId){
        // POST_FIND
        historyService.addHistoryEntry(postId, PostState.POST_FIND);
        try {
            PostDTO postDTO = apiConsumerFeign.getPostById(postId);
            Post post = new Post(postDTO.getId(), postDTO.getTitle(), postDTO.getBody());
            postRepository.save(post);
            postOk(postId);
        } catch (Exception exception) {
            postFailed(postId);
        }

    }

    public void postOk(Long postId){
        // POST_OK
        historyService.addHistoryEntry(postId, PostState.POST_OK);
        messageProducer.sendMessage("COMMENTS_FIND", postId);
    }

    public void findComments(Long postId){
        // COMMENTS_FIND
        historyService.addHistoryEntry(postId, PostState.COMMENTS_FIND);
        try {
            Post post = postRepository.findById(postId).orElse(null);
            List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
            commentService.saveComments(comments, post);
            commentsOk(postId);
        } catch (Exception exception) {
            postFailed(postId);
        }
    }

    public void commentsOk(Long postId){
        //COMMENTS_OK
        historyService.addHistoryEntry(postId, PostState.COMMENTS_OK);
        enablePost(postId);
    }

    public void enablePost(Long postId){
        // ENABLED
        historyService.addHistoryEntry(postId, PostState.ENABLED);
    }

    public void postFailed(Long postId){
        // FAILED
        historyService.addHistoryEntry(postId, PostState.FAILED);
        // DISABLED
        historyService.addHistoryEntry(postId, PostState.DISABLED);
    }

    //DISABLE - DELETE
    public void disablePost(Long postId) {
        validatePostId(postId);
        validatePostExists(postId);
        if (!historyService.isPostInState(postId, PostState.ENABLED)) {
            throw new InvalidPostStateException("Post with ID " + postId + " is not in the ENABLED state.");
        }
        //DISABLED
        historyService.addHistoryEntry(postId, PostState.DISABLED);
    }

    //REPROCESS - PUT
    public void reprocessPost(Long postId) {
        validatePostId(postId);
        validatePostExists(postId);
        if (!historyService.isPostInState(postId, PostState.DISABLED) && !historyService.isPostInState(postId, PostState.ENABLED)) {
            throw new InvalidPostStateException("Post with ID " + postId + " is not in the ENABLED or DISABLED state.");
        }
        // UPDATING
        historyService.addHistoryEntry(postId, PostState.UPDATING);
        messageProducer.sendMessage("POST_FIND", postId);
    }

    //QUERY - GET
    public List<PostCompleteDTO> queryPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostCompleteDTO> result = new ArrayList<>();

        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (Comment comment : comments) {
                commentDTOs.add(new CommentDTO(comment.getId(), post.getId(), comment.getBody()));
            }

            List<History> historyList = historyRepository.findHistoryByPostId(post.getId());
            List<HistoryDTO> historyDTOs = new ArrayList<>();
            for (History history : historyList) {
                Long historyId = history.getId() != null ? history.getId() : 0L;
                historyDTOs.add(new HistoryDTO(historyId, post.getId(), history.getDate(), history.getState()));
            }

            result.add(new PostCompleteDTO(post.getId(), post.getTitle(), post.getBody(), commentDTOs, historyDTOs));
        }

        return result;
    }

    // ID VALIDATION METHODS
    void validatePostId(long id) {
        boolean validId = id >= 1 && id <= 100;
        if (!validId) {
            throw new IllegalArgumentException("Id must be a number between 1 and 100");
        }
    }

    void validatePostIsNotDuplicated(long id) {
        boolean exists = postRepository.existsById(id);
        if (exists) {
            throw new DataIntegrityViolationException("A post with id= " + id + " already exists");
        }
    }

    void validatePostExists(long id) {
        boolean exists = postRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Post not found with id:" + id);
        }
    }


}




