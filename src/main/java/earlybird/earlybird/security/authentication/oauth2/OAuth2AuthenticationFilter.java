package earlybird.earlybird.security.authentication.oauth2;

import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.security.enums.OAuth2ProviderName;
import earlybird.earlybird.security.token.jwt.access.CreateJWTAccessTokenService;
import earlybird.earlybird.security.token.jwt.refresh.CreateJWTRefreshTokenService;
import earlybird.earlybird.security.token.jwt.refresh.JWTRefreshTokenToCookieService;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import earlybird.earlybird.security.token.oauth2.service.CreateOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.service.DeleteOAuth2TokenService;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/login", "POST");
    private final CreateJWTAccessTokenService createJWTAccessTokenService;
    private final CreateJWTRefreshTokenService createJWTRefreshTokenService;
    private final JWTRefreshTokenToCookieService jwtRefreshTokenToCookieService;
    private final CreateOAuth2TokenService createOAuth2TokenService;
    private final DeleteOAuth2TokenService deleteOAuth2TokenService;

    public OAuth2AuthenticationFilter(CreateJWTAccessTokenService createJWTAccessTokenService,
                                      CreateJWTRefreshTokenService createJWTRefreshTokenService,
                                      JWTRefreshTokenToCookieService jwtRefreshTokenToCookieService,
                                      CreateOAuth2TokenService createOAuth2TokenService,
                                      DeleteOAuth2TokenService deleteOAuth2TokenService) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.createJWTAccessTokenService = createJWTAccessTokenService;
        this.createJWTRefreshTokenService = createJWTRefreshTokenService;
        this.jwtRefreshTokenToCookieService = jwtRefreshTokenToCookieService;
        this.createOAuth2TokenService = createOAuth2TokenService;
        this.deleteOAuth2TokenService = deleteOAuth2TokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String oauth2ProviderName = request.getHeader("Provider-Name");
        String oauth2AccessToken = request.getHeader("OAuth2-Access");
        String oauth2RefreshToken = request.getHeader("OAuth2-Refresh");

        if (oauth2ProviderName == null || oauth2AccessToken == null || oauth2RefreshToken == null) {
            throw new AuthenticationServiceException("provider-name 또는 oauth2-access 값이 제공되지 않았습니다.");
        }

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(oauth2ProviderName, oauth2AccessToken);

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final int accessTokenExpiredMs = 60 * 60 * 60; // 60 * 60 * 60 ms = 36분
        final int refreshTokenExpiredMs = 86400000; // 86400000 ms = 24 h

        UserAccountInfoDTO userDTO = ((OAuth2UserDetails) authResult.getPrincipal()).getUserAccountInfoDTO();
        String access = createJWTAccessTokenService.createAccessToken(userDTO, (long) accessTokenExpiredMs);
        String refresh = createJWTRefreshTokenService.createRefreshToken(userDTO, (long) refreshTokenExpiredMs);

        response.setHeader("access", access);
        response.addCookie(jwtRefreshTokenToCookieService.createCookie(refresh, refreshTokenExpiredMs));

        deleteOAuth2TokenService.deleteByUserId(userDTO.getId());

        OAuth2TokenDTO oAuth2TokenDTO = OAuth2TokenDTO.builder()
                .userDTO(userDTO)
                .accessToken(request.getHeader("OAuth2-Access"))
                .refreshToken(request.getHeader("OAuth2-Refresh"))
                .oAuth2ProviderName(OAuth2ProviderName.valueOf(request.getHeader("Provider-Name").toUpperCase()))
                .build();

        createOAuth2TokenService.create(oAuth2TokenDTO);
    }
}
