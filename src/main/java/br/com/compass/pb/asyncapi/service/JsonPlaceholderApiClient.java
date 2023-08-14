package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.Post;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class JsonPlaceholderApiClient {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private final RestTemplate restTemplate;

    public JsonPlaceholderApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public Post getPostById(Long postId) {
        String url = BASE_URL + "/posts/" + postId;
        return restTemplate.getForObject(url, Post.class);
    }

    public List<Post> getAllPosts() {
        String url = BASE_URL + "/posts";
        Post[] postsArray = restTemplate.getForObject(url, Post[].class);
        return Arrays.asList(postsArray);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        String url = BASE_URL + "/comments?postId=" + postId;
        Comment[] commentsArray = restTemplate.getForObject(url, Comment[].class);
        return Arrays.asList(commentsArray);
    }

}

