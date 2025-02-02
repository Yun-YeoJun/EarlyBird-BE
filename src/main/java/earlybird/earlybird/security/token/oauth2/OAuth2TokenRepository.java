package earlybird.earlybird.security.token.oauth2;

import earlybird.earlybird.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OAuth2TokenRepository extends JpaRepository<OAuth2Token, Long> {

  Optional<OAuth2Token> findByUser(User user);

  @Transactional
  void deleteByUser(User user);
}
