package br.com.compass.pb.asyncapi.client;

import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "api-consumer", url= "https://jsonplaceholder.typicode.com/posts")
public interface ApiConsumerFeign {
    @GetMapping(value= "/{id}")
    Post getPostById(@PathVariable("id") Long id);

    @GetMapping(value= "/{id}/comments")
    List<Comment> getCommentByPostId(@PathVariable("id") Long id);
}
