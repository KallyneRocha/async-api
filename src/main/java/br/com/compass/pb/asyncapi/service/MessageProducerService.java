package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.dto.PostMessagePayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageForPostProcessing(Long postId) {
        PostMessagePayload payload = new PostMessagePayload(postId, "PROCESS");
        rabbitTemplate.convertAndSend("post_processing_queue", payload);
    }

    public void sendMessageForPostDisabling(Long postId) {
        PostMessagePayload payload = new PostMessagePayload(postId, "DISABLE");
        rabbitTemplate.convertAndSend("post_disabling_queue", payload);
    }

    public void sendMessageForPostReprocessing(Long postId) {
        PostMessagePayload payload = new PostMessagePayload(postId, "REPROCESS");
        rabbitTemplate.convertAndSend("post_reprocessing_queue", payload);
    }

    public void sendMessageForPostFailed(Long postId) {
        PostMessagePayload payload = new PostMessagePayload(postId, "FAILED");
        rabbitTemplate.convertAndSend("post_failed_queue", payload);
    }
}
