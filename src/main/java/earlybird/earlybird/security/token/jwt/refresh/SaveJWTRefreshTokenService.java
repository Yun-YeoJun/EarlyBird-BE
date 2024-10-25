package earlybird.earlybird.security.token.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

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
