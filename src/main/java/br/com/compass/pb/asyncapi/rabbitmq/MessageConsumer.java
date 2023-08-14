package br.com.compass.pb.asyncapi.rabbitmq;

import br.com.compass.pb.asyncapi.service.PostService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final PostService postService;

    @Autowired
    public MessageConsumer(PostService postService) {
        this.postService = postService;
    }

    @RabbitListener(queues = "post_processing_queue")
    public void processPost(Long postId) {
        postService.processPost(postId);
    }

    @RabbitListener(queues = "post_disabling_queue")
    public void disablePost(Long postId) {
        postService.disablePost(postId);
    }

    @RabbitListener(queues = "post_reprocessing_queue")
    public void reprocessPost(Long postId) {
        postService.reprocessPost(postId);
    }
}
