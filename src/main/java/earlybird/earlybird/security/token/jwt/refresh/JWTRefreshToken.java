package earlybird.earlybird.security.token.jwt.refresh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class JWTRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId;
    private String refreshToken;
    private String expiration;

    public JWTRefreshToken() {

    }

    public JWTRefreshToken(String accountId, String refreshToken, String expiration) {
        this.accountId = accountId;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
