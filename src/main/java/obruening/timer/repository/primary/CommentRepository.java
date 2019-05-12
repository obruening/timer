package obruening.timer.repository.primary;

import org.springframework.data.jpa.repository.JpaRepository;

import obruening.timer.model.primary.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}