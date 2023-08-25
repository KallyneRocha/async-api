package br.com.compass.pb.asyncapi.controller;

import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.service.MessageProducerService;
import br.com.compass.pb.asyncapi.service.PostService;
import br.com.compass.pb.asyncapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {


    private final PostService postService;
    private final MessageProducerService messageProducerService;

    @Autowired
    public PostController(PostService postService, MessageProducerService messageProducerService) {
        this.postService = postService;
        this.messageProducerService = messageProducerService;
    }

    //PROCESS POST
    @PostMapping("/{postId}")
    public ResponseEntity<String> processPost(@PathVariable Long postId) {
        postService.processPost(postId);
        return ResponseEntity.ok("Post processing succeed.");
    }
    //DISABLE POST
    @DeleteMapping("/{postId}")
    public ResponseEntity disablePost(@PathVariable Long postId) {
        postService.disablePost(postId);
        return ResponseEntity.ok("Post disabling succeed.");
    }
    //REPROCESS POST
    @PutMapping("/{postId}")
    public ResponseEntity reprocessPost(@PathVariable Long postId) {
        postService.reprocessPost(postId);
        return ResponseEntity.ok("Post reprocessing succeed.");
    }
    //QUERY POSTS
    @GetMapping
    public ApiResponse queryPosts() {
        List<Post> posts = postService.queryPosts();
        return ApiResponse.success(posts, "Posts retrieved successfully.");
    }



    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

}

