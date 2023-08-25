package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.client.ApiConsumerFeign;
import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import br.com.compass.pb.asyncapi.exception.InvalidPostStateException;
import br.com.compass.pb.asyncapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final ApiConsumerFeign apiConsumerFeign;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final HistoryService historyService;
    private final MessageProducerService messageProducerService;

    @Autowired
    public PostService(ApiConsumerFeign apiConsumerFeign, PostRepository postRepository, CommentService commentService,
                       HistoryService historyService, MessageProducerService messageProducerService) {
        this.apiConsumerFeign = apiConsumerFeign;
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.historyService = historyService;
        this.messageProducerService = messageProducerService;
    }

    public Post getPostById(Long id) {
        Post post = apiConsumerFeign.getPostById(id);
        //.orElseThrow(() -> new EntityNotFoundException());
        return post;
    }

    public Post processPost(Long postId) {
        //o process post precisa criar o post vazio com historico, pegar conteudo do post da api,
        // juntar com os comentarios e salvar no banco h2

        //criar post vazio e setar created
        Post post = new Post();
        post.setId(postId);
        postRepository.save(post);
        // CREATED
        historyService.addHistoryEntry(post, PostState.CREATED);

        // POST_FIND (enviar primeira msg)
        historyService.addHistoryEntry(post, PostState.POST_FIND);
        Post postDTO = apiConsumerFeign.getPostById(postId);

        boolean postFound;
        if (postDTO != null) {
            postFound = postDTO.getId().equals(postId);
        }else {
            postFound = false;
        }

        if (postFound){
            //POST_OK
            historyService.addHistoryEntry(post, PostState.POST_OK);

            post.setTitle(postDTO.getTitle());
            post.setBody(postDTO.getBody());
            postRepository.save(post);

            //COMMENTS_FIND (mandar msg)
            historyService.addHistoryEntry(post, PostState.COMMENTS_FIND);
            List<Comment> comments = apiConsumerFeign.getCommentByPostId(postId);

            if (!comments.isEmpty()) {
                //COMMENTS_OK
                historyService.addHistoryEntry(post, PostState.COMMENTS_OK);


                commentService.saveComments(comments);

                //ENABLED
                historyService.addHistoryEntry(post, PostState.ENABLED);
                return post;
            } else {
                // if comments not found
                historyService.addHistoryEntry(post, PostState.FAILED);
                messageProducerService.sendMessageForPostFailed(postId);
            }
        }else {
            // if post not found
            historyService.addHistoryEntry(post, PostState.FAILED);
            messageProducerService.sendMessageForPostFailed(postId);
        }

        historyService.addHistoryEntry(post, PostState.DISABLED);
        return post;

       /* if (postId >= 1 && postId <= 100 && !postRepository.existsById(postId)) {
            Post post = new Post();
            post.setId(postId);
            postRepository.save(post);
            // CREATED
            historyService.addHistoryEntry(post, PostState.CREATED);
            // POST_FIND
            historyService.addHistoryEntry(post, PostState.POST_FIND);

            boolean postFound = checkIfPostIsFound(postId);
            if (postFound) {
                //POST_OK
                historyService.addHistoryEntry(post, PostState.POST_OK);
                //COMMENTS_FIND
                historyService.addHistoryEntry(post, PostState.COMMENTS_FIND);
                //mudar pra metodo da api
                List<Comment> comments = commentService.getCommentsByPostId(postId);

                if (!comments.isEmpty()) {
                    //COMMENTS_OK
                    historyService.addHistoryEntry(post, PostState.COMMENTS_OK);
                    //ENABLED
                    historyService.addHistoryEntry(post, PostState.ENABLED);
                    post.setHistory(historyService.getHistoryByPostId(postId));
                    post.setComments(comments);
                    return post;
                } else {
                    // if comments not found
                    historyService.addHistoryEntry(post, PostState.FAILED);
                    messageProducerService.sendMessageForPostFailed(postId);
                }
            } else {
                // if post not found
                historyService.addHistoryEntry(post, PostState.FAILED);
                messageProducerService.sendMessageForPostFailed(postId);
            }
        } else {
            messageProducerService.sendMessageForPostFailed(postId);
        }*/
    }

    public void disablePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        if (!historyService.isPostInState(post, PostState.ENABLED)) {
            throw new InvalidPostStateException("Post with ID " + postId + " is not in the ENABLED state.");
        }

        historyService.addHistoryEntry(post, PostState.DISABLED);

        //messageProducerService.sendMessageForPostDisabling(post.getId());
    }

    public void reprocessPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        if (!historyService.isPostInState(post, PostState.DISABLED) && !historyService.isPostInState(post, PostState.ENABLED)) {
            throw new InvalidPostStateException("Post with ID " + postId + " is not in the ENABLED or DISABLED state.");
        }
        //UPDATING
        historyService.addHistoryEntry(post, PostState.UPDATING);
        reprocessingPost(postId);
    }

    public Post reprocessingPost(Long postId) {
        //o process post precisa criar o post vazio com historico, pegar conteudo do post da api,
        // juntar com os comentarios e salvar no banco h2

        //criar post vazio e setar created

        Post post = new Post();
        post.setId(postId);

        //deletar History
        historyService.deleteHistory(postId);
        //deletar Comments
        commentService.deleteComments(postId);

        // POST_FIND (enviar primeira msg)
        historyService.addHistoryEntry(post, PostState.POST_FIND);
        Post postDTO = apiConsumerFeign.getPostById(postId);

        boolean postFound;
        if (postDTO != null) {
            postFound = postDTO.getId().equals(postId);
        }else {
            postFound = false;
        }

        if (postFound){
            //POST_OK
            historyService.addHistoryEntry(post, PostState.POST_OK);

            post.setTitle(postDTO.getTitle());
            post.setBody(postDTO.getBody());
            postRepository.save(post);

            //COMMENTS_FIND (mandar msg)
            historyService.addHistoryEntry(post, PostState.COMMENTS_FIND);
            List<Comment> comments = apiConsumerFeign.getCommentByPostId(postId);

            if (!comments.isEmpty()) {
                //COMMENTS_OK
                historyService.addHistoryEntry(post, PostState.COMMENTS_OK);

                commentService.saveComments(comments);

                //ENABLED
                historyService.addHistoryEntry(post, PostState.ENABLED);
                return post;
            } else {
                // if comments not found
                historyService.addHistoryEntry(post, PostState.FAILED);
                messageProducerService.sendMessageForPostFailed(postId);
            }
        }else {
            // if post not found
            historyService.addHistoryEntry(post, PostState.FAILED);
            messageProducerService.sendMessageForPostFailed(postId);
        }

        historyService.addHistoryEntry(post, PostState.DISABLED);
        return post;
    }

    public List<Post> queryPosts() {
        List<Post> CompletePosts = new ArrayList<>();
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            List<Comment> comments = commentService.getCommentsByPostId(post.getId());
            List<History> history = historyService.getHistoryByPostId(post.getId());

            Post postComplete = new Post();
            postComplete.setId(post.getId());
            postComplete.setTitle(post.getTitle());
            postComplete.setBody(post.getBody());
            postComplete.setComments(comments);
            postComplete.setHistory(history);

            CompletePosts.add(postComplete);
        }

        return CompletePosts;
    }

}




