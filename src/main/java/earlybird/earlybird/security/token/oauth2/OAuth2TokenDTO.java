package earlybird.earlybird.security.token.oauth2;

import earlybird.earlybird.security.enums.OAuth2ProviderName;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuth2TokenDTO {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private OAuth2ProviderName oAuth2ProviderName;
    private UserAccountInfoDTO userDTO;
}
