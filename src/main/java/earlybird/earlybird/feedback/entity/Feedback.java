package earlybird.earlybird.feedback.entity;

import earlybird.earlybird.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Feedback {

    @Column(name = "feedback_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "feedback_content")
    private String content;

    @Column(name = "feedback_nps_score", nullable = false)
    private Long score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "feedback_created_at", nullable = false)
    private LocalDateTime createdAt;
}
