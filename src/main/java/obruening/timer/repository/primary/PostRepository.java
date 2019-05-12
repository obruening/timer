package obruening.timer.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;

import obruening.timer.model.primary.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}