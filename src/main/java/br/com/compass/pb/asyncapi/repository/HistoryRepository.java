package br.com.compass.pb.asyncapi.repository;

import br.com.compass.pb.asyncapi.entity.History;
import br.com.compass.pb.asyncapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findHistoryByPostId(Long postId);

}

