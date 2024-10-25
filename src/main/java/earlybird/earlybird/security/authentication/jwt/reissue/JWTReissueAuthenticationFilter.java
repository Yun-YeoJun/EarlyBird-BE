package earlybird.earlybird.security.authentication.jwt.reissue;

import earlybird.earlybird.security.token.jwt.access.CreateJWTAccessTokenService;
import earlybird.earlybird.security.token.jwt.refresh.CreateJWTRefreshTokenService;
import earlybird.earlybird.security.token.jwt.refresh.JWTRefreshTokenRepository;
import earlybird.earlybird.security.token.jwt.refresh.JWTRefreshTokenToCookieService;
import earlybird.earlybird.security.token.jwt.refresh.SaveJWTRefreshTokenService;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JWTReissueAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/reissue", "POST");
    private final CreateJWTRefreshTokenService createJWTRefreshTokenService;
    private final CreateJWTAccessTokenService createJWTAccessTokenService;
    private final JWTRefreshTokenRepository JWTRefreshTokenRepository;
    private final SaveJWTRefreshTokenService saveJWTRefreshTokenService;
    private final JWTRefreshTokenToCookieService JWTRefreshTokenToCookieService;

    public JWTReissueAuthenticationFilter(
            CreateJWTAccessTokenService createJWTAccessTokenService,
            CreateJWTRefreshTokenService createJWTRefreshTokenService,
            JWTRefreshTokenRepository JWTRefreshTokenRepository,
            SaveJWTRefreshTokenService saveJWTRefreshTokenService,
            JWTRefreshTokenToCookieService JWTRefreshTokenToCookieService
    ) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.createJWTAccessTokenService = createJWTAccessTokenService;
        this.createJWTRefreshTokenService = createJWTRefreshTokenService;
        this.JWTRefreshTokenRepository = JWTRefreshTokenRepository;
        this.saveJWTRefreshTokenService = saveJWTRefreshTokenService;
        this.JWTRefreshTokenToCookieService = JWTRefreshTokenToCookieService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        Optional<Cookie> optionalRefreshCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh"))
                .findFirst();

        if (optionalRefreshCookie.isEmpty()) {
            throw new AuthenticationServiceException("refresh 토큰이 존재하지 않습니다.");
        }

        String refreshToken = optionalRefreshCookie.get().getValue();
        JWTReissueAuthenticationToken authToken = new JWTReissueAuthenticationToken(refreshToken);

        return this.getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final int accessTokenExpiredMs = 60 * 60 * 60; // 60 * 60 * 60 ms = 36분
        final int refreshTokenExpiredMs = 86400000; // 86400000 ms = 24 h

        UserAccountInfoDTO userInfo = (UserAccountInfoDTO) authResult.getPrincipal();
        String accountId = userInfo.getAccountId();
        String role = userInfo.getRole();

        String newAccessToken = createJWTAccessTokenService.createAccessToken(userInfo, (long) accessTokenExpiredMs);
        String newRefreshToken = createJWTRefreshTokenService.createRefreshToken(userInfo, (long) refreshTokenExpiredMs);

        String oldRefreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh"))
                .findFirst()
                .get()
                .getValue();

        JWTRefreshTokenRepository.deleteByRefreshToken(oldRefreshToken);

        response.setHeader("access", newAccessToken);
        response.addCookie(JWTRefreshTokenToCookieService.createCookie(newRefreshToken, refreshTokenExpiredMs));
    }
}
