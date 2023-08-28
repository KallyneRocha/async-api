package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import br.com.compass.pb.asyncapi.queues.MessageProducer;
import br.com.compass.pb.asyncapi.service.PostService;
import br.com.compass.pb.asyncapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private HistoryService historyService;
    @Mock
    private MessageProducer messageProducer;

    private PostState postState;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create post and send message")
    public void testCreatePost() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        postService.createPost(postId);

        verify(postRepository).save(any(Post.class));
        verify(historyService).addHistoryEntry(eq(postId), eq(PostState.CREATED));
        verify(messageProducer).sendMessage(eq("POST_FIND"), eq(postId));
    }
    @Test
    @DisplayName("Should disable post")
    public void testDisablePost() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);
        when(historyService.isPostInState(postId, PostState.ENABLED)).thenReturn(true);

        postService.disablePost(postId);

        verify(historyService).addHistoryEntry(eq(postId), eq(PostState.DISABLED));
    }

    @Test
    @DisplayName("Should reprocess post and send message")
    public void testReprocessPost() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);
        when(historyService.isPostInState(postId, PostState.DISABLED)).thenReturn(true);
        when(historyService.isPostInState(postId, PostState.ENABLED)).thenReturn(false);

        postService.reprocessPost(postId);

        verify(historyService).addHistoryEntry(eq(postId), eq(PostState.UPDATING));
        verify(messageProducer).sendMessage(eq("POST_FIND"), eq(postId));
    }

    @Test
    @DisplayName("Should throw exception for invalid ID")
    public void testValidPostId_GreaterThan100() {
        assertThrows(IllegalArgumentException.class, () -> {
            postService.validatePostId(101);
        });
    }

    @Test
    @DisplayName("Should throw exception for duplicate ID")
    public void testPostIsNotDuplicated() {
        when(postRepository.existsById(1L)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> {
            postService.validatePostIsNotDuplicated(1);
        });

        verify(postRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should throw exception for non-existing ID")
    public void testPostExists() {
        when(postRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            postService.validatePostExists(1);
        });

        verify(postRepository, times(1)).existsById(1L);
    }
}