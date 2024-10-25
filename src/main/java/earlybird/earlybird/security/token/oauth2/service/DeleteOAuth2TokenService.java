package earlybird.earlybird.security.token.oauth2.service;

import earlybird.earlybird.security.token.oauth2.OAuth2TokenRepository;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import earlybird.earlybird.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteOAuth2TokenService {

    private final OAuth2TokenRepository oAuth2TokenRepository;
    private final UserRepository userRepository;

    public void deleteById(Long id) {
        oAuth2TokenRepository.deleteById(id);
    }

    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        oAuth2TokenRepository.deleteByUser(user);
    }

    public void deleteByUserAccountInfoDTO(UserAccountInfoDTO userInfo) {
        User user = userRepository.findById(userInfo.getId()).orElseThrow(UserNotFoundException::new);
        oAuth2TokenRepository.deleteByUser(user);
    }
}
