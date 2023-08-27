package br.com.compass.pb.asyncapi.queues;

import br.com.compass.pb.asyncapi.service.PostService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final PostService postService;

    public MessageConsumer(PostService postService) {
        this.postService = postService;
    }

    @JmsListener(destination = "POST_FIND")
    public void findPost(Long id) { postService.findPost(id); }

    @JmsListener(destination = "COMMENTS_FIND")
    public void findComments(Long postId) {
        postService.findComments(postId);
    }
}

