package earlybird.earlybird.feedback.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackCommentRepository extends JpaRepository<FeedbackComment, Long> {
}
