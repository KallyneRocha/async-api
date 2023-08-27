
package br.com.compass.pb.asyncapi.service;

import br.com.compass.pb.asyncapi.entity.Post;
import br.com.compass.pb.asyncapi.enums.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.repository.HistoryRepository;
import br.com.compass.pb.asyncapi.repository.PostRepository;

import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final PostRepository postRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, PostRepository postRepository) {
        this.historyRepository = historyRepository;
        this.postRepository = postRepository;
    }

    public List<History> getHistoryByPostId(Long postId) {
        return historyRepository.findHistoryByPostId(postId);
    }

    public void addHistoryEntry(Long postId, PostState status) {
        Post post = postRepository.findById(postId).get();
        History history = new History(status, post);
        historyRepository.save(history);
    }

    public boolean isPostInState(Long postId, PostState state) {
        List<History> historyEntries = historyRepository.findHistoryByPostId(postId);

        if (!historyEntries.isEmpty()) {
            History latestHistoryEntry = historyEntries.get(historyEntries.size() -1);
            return latestHistoryEntry.getState() == state;
        }

        return false;
    }
}


