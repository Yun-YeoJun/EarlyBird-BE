package earlybird.earlybird.user.service;

import earlybird.earlybird.error.exception.UserNotFoundException;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteUserService {

  private final UserRepository userRepository;

  public void deleteUser(UserAccountInfoDTO userInfo) {
    String accountId = userInfo.getAccountId();
    Optional<User> optionalUser = userRepository.findByAccountId(accountId);
    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException();
    }
    User user = optionalUser.get();
    userRepository.delete(user);
  }
}
