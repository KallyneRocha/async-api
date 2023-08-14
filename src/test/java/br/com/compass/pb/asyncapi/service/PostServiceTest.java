package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Comment;
import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import br.com.compass.pb.asyncapi.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private HistoryService historyService;

    @Test
    public void testQueryPosts() {
    }
}
