package earlybird.earlybird.feedback.repository;

import earlybird.earlybird.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
