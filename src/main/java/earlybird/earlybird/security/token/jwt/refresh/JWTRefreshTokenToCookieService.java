package earlybird.earlybird.security.token.jwt.refresh;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class JWTRefreshTokenToCookieService {

    public Cookie createCookie(String refresh, int expiredMs) {
        Cookie cookie = new Cookie("refresh", refresh);
        cookie.setMaxAge(expiredMs);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
