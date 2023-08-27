package br.com.compass.pb.asyncapi.client;

import br.com.compass.pb.asyncapi.dto.CommentDTO;
import br.com.compass.pb.asyncapi.dto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "api-consumer", url= "https://jsonplaceholder.typicode.com/posts")
public interface ApiConsumerFeign {
    @GetMapping(value= "/{id}")
    PostDTO getPostById(@PathVariable("id") Long id);

    @GetMapping(value= "/{id}/comments")
    List<CommentDTO> getCommentByPostId(@PathVariable("id") Long id);
}
