package br.com.compass.pb.asyncapi.controller;

import br.com.compass.pb.asyncapi.dto.PostCompleteDTO;
import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.queues.MessageProducer;
import br.com.compass.pb.asyncapi.service.PostService;
import br.com.compass.pb.asyncapi.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MessageProducer messageProducer;

    @Autowired
    public PostController(PostService postService, MessageProducer messageProducer) {
        this.postService = postService;
        this.messageProducer = messageProducer;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<String> processPost(@PathVariable Long postId) {
        postService.createPost(postId);
        return ResponseEntity.ok("Post processing succeed.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity disablePost(@PathVariable Long postId) {
        postService.disablePost(postId);
        return ResponseEntity.ok("Post disabling succeed.");
    }

    @PutMapping("/{postId}")
    public ResponseEntity reprocessPost(@PathVariable Long postId) {
        postService.reprocessPost(postId);
        return ResponseEntity.ok("Post reprocessing succeed.");
    }

    @GetMapping
    public ApiResponse queryPosts() {
        List<PostCompleteDTO> posts = postService.queryPosts();
        return ApiResponse.success(posts, "Posts retrieved successfully.");
    }

}

