package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.repository.HistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<History> getHistoryByPostId(Long postId) {
        return historyRepository.findHistoryByPostId(postId);
    }

    public void addHistoryEntry(Post post, PostState status) {
        History history = new History(status, post);
        historyRepository.save(history);
    }

    public boolean isPostInState(Post post, PostState state) {
        List<History> historyEntries = historyRepository.findHistoryByPostId(post.getId());

        if (!historyEntries.isEmpty()) {
            History latestHistoryEntry = historyEntries.get(0);
            return latestHistoryEntry.getStatus() == state;
        }

        return false;
    }
}

