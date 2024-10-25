package earlybird.earlybird.security.token.oauth2.service;

import earlybird.earlybird.security.token.oauth2.OAuth2Token;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenRepository;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import earlybird.earlybird.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FindOAuth2TokenService {

    private final OAuth2TokenRepository oAuth2TokenRepository;
    private final UserRepository userRepository;

    public OAuth2TokenDTO findByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(UserNotFoundException::new);
        Optional<OAuth2Token> optionalOAuth2Token = oAuth2TokenRepository.findByUser(user);
        OAuth2Token oAuth2Token = optionalOAuth2Token.orElseThrow();
        return oAuth2Token.toOAuth2TokenDTO();
    }
}
