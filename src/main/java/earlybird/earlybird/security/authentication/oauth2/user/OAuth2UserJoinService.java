package earlybird.earlybird.security.authentication.oauth2.user;

import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserJoinService {

  private final UserRepository userRepository;

  public void join(OAuth2ServerResponse userInfo) {
    User user = new User(userInfo);
    userRepository.save(user);
  }
}
