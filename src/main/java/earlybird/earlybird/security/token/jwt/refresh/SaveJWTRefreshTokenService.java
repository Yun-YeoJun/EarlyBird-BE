package earlybird.earlybird.security.token.jwt.refresh;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveJWTRefreshTokenService {

  private final JWTRefreshTokenRepository JWTRefreshTokenRepository;

  public void saveJWTRefreshToken(String accountId, String refresh, Long expiredMs) {
    Date date = new Date(System.currentTimeMillis() + expiredMs);
    JWTRefreshToken JWTRefreshToken = new JWTRefreshToken(accountId, refresh, date.toString());
    JWTRefreshTokenRepository.save(JWTRefreshToken);
  }
}
