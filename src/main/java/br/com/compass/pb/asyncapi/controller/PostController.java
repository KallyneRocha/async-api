package br.com.compass.pb.asyncapi.controller;

import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.service.MessageProducerService;
import br.com.compass.pb.asyncapi.service.PostService;
import br.com.compass.pb.asyncapi.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MessageProducerService messageProducerService;

    public PostController(PostService postService, MessageProducerService messageProducerService) {
        this.postService = postService;
        this.messageProducerService = messageProducerService;
    }

    @PostMapping("/{postId}")
    public ApiResponse processPost(@PathVariable Long postId) {
        try {
            postService.processPost(postId);
            return ApiResponse.success(null, "Post processing initiated.");
        } catch (Exception e) {
            return ApiResponse.error("Error processing post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{postId}")
    public ApiResponse disablePost(@PathVariable Long postId) {
        try {
            postService.disablePost(postId);
            return ApiResponse.success(null, "Post disabling initiated.");
        } catch (Exception e) {
            return ApiResponse.error("Error disabling post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{postId}")
    public ApiResponse reprocessPost(@PathVariable Long postId) {
        try {
            postService.reprocessPost(postId);
            return ApiResponse.success(null, "Post reprocessing initiated.");
        } catch (Exception e) {
            return ApiResponse.error("Error reprocessing post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ApiResponse queryPosts() {
        try {
            List<Post> posts = postService.queryPosts();
            return ApiResponse.success(posts, "Posts retrieved successfully.");
        } catch (Exception e) {
            return ApiResponse.error("Error querying posts: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

