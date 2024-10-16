package earlybird.earlybird.user.entity;

import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 스프링 시큐리티에서 사용하는 값
     */
    @Column(unique = true)
    private String accountId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    public User(OAuth2ServerResponse userInfo) {
        this.accountId = userInfo.getProviderName() + " " + userInfo.getProviderId();
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
        this.role = "USER";
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
