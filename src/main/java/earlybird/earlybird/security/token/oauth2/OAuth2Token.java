package earlybird.earlybird.security.token.oauth2;

import earlybird.earlybird.security.enums.OAuth2ProviderName;
import earlybird.earlybird.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static lombok.AccessLevel.PRIVATE;

@Builder
@AllArgsConstructor(access = PRIVATE)
@Entity
public class OAuth2Token {

    @Column(name = "oauth2_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String accessToken;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private OAuth2ProviderName oAuth2ProviderName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public OAuth2Token() {

    }

    public OAuth2TokenDTO toOAuth2TokenDTO() {

        return OAuth2TokenDTO.builder()
                .id(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .oAuth2ProviderName(oAuth2ProviderName)
                .userDTO(user.toUserAccountInfoDTO())
                .build();
    }
}
