package earlybird.earlybird.user.entity;

import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 스프링 시큐리티에서 사용하는 값
     */
    @Column(name = "user_account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_role", nullable = false)
    private String role;

    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private User(Long id, String accountId, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(OAuth2ServerResponse userInfo) {
        this.accountId = userInfo.getProviderName() + " " + userInfo.getProviderId();
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
        this.role = "USER";
        this.createdAt = LocalDateTime.now();
    }

    public User() {

    }

    public UserAccountInfoDTO toUserAccountInfoDTO() {
        return UserAccountInfoDTO.builder()
                .id(id)
                .accountId(accountId)
                .name(name)
                .email(email)
                .role(role)
                .build();
    }
}
