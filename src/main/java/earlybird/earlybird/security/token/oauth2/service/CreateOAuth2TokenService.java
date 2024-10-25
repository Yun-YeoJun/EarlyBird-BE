package earlybird.earlybird.security.token.oauth2.service;

import earlybird.earlybird.security.token.oauth2.OAuth2Token;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenRepository;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import earlybird.earlybird.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateOAuth2TokenService {

    private final OAuth2TokenRepository oAuth2TokenRepository;
    private final UserRepository userRepository;

    public void create(OAuth2TokenDTO oAuth2TokenDTO) {
        User user = userRepository.findById(oAuth2TokenDTO.getUserDTO().getId()).orElseThrow(UserNotFoundException::new);

        OAuth2Token oAuth2Token = OAuth2Token.builder()
                .accessToken(oAuth2TokenDTO.getAccessToken())
                .refreshToken(oAuth2TokenDTO.getRefreshToken())
                .oAuth2ProviderName(oAuth2TokenDTO.getOAuth2ProviderName())
                .user(user)
                .build();

        oAuth2TokenRepository.save(oAuth2Token);
    }
}
