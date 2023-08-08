package br.com.compass.pb.asyncapi.repository;

import br.com.compass.pb.asyncapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository <Post, Long> {
}
