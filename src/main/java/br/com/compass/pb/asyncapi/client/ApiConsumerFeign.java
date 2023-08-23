package br.com.compass.pb.asyncapi.client;

import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value= "api-consumer", url= "https://jsonplaceholder.typicode.com")
public interface ApiConsumerFeign {
    @GetMapping(value= "/{id}")
    Post getPostById(@PathVariable("id") Long id);

    @GetMapping(value= "/{id}/comments")
    <List>Comment getCommentById(@PathVariable("id") Long id);
}
