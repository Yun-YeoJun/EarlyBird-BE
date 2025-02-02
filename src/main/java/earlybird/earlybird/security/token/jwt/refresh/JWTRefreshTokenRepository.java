package earlybird.earlybird.security.token.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JWTRefreshTokenRepository extends JpaRepository<JWTRefreshToken, Long> {
    @Transactional
    void deleteByRefreshToken(String refresh);
}
