package earlybird.earlybird.security.config;

import earlybird.earlybird.security.authentication.jwt.JWTAuthenticationFilter;
import earlybird.earlybird.security.authentication.jwt.reissue.JWTReissueAuthenticationFilter;
import earlybird.earlybird.security.authentication.jwt.reissue.JWTReissueAuthenticationProvider;
import earlybird.earlybird.security.authentication.oauth2.OAuth2AuthenticationFilter;
import earlybird.earlybird.security.authentication.oauth2.OAuth2AuthenticationProvider;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserJoinService;
import earlybird.earlybird.security.token.jwt.JWTUtil;
import earlybird.earlybird.security.token.jwt.access.CreateJWTAccessTokenService;
import earlybird.earlybird.security.token.jwt.refresh.CreateJWTRefreshTokenService;
import earlybird.earlybird.security.token.jwt.refresh.JWTRefreshTokenRepository;
import earlybird.earlybird.security.token.jwt.refresh.JWTRefreshTokenToCookieService;
import earlybird.earlybird.security.token.jwt.refresh.SaveJWTRefreshTokenService;
import earlybird.earlybird.security.token.oauth2.service.CreateOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.service.DeleteOAuth2TokenService;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JWTRefreshTokenRepository JWTRefreshTokenRepository;
  private final UserDetailsService userDetailsService;
  private final OAuth2UserJoinService oAuth2UserJoinService;
  private final CreateJWTAccessTokenService createJWTAccessTokenService;
  private final CreateJWTRefreshTokenService createJWTRefreshTokenService;
  private final JWTRefreshTokenToCookieService JWTRefreshTokenToCookieService;
  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;
  private final SaveJWTRefreshTokenService saveJWTRefreshTokenService;
  private final CreateOAuth2TokenService createOAuth2TokenService;
  private final DeleteOAuth2TokenService deleteOAuth2TokenService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(
        new OAuth2AuthenticationProvider(userDetailsService, oAuth2UserJoinService));
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http.authenticationManager(authenticationManager);

    http.authorizeHttpRequests(
        auth ->
            auth
                //                        .anyRequest().authenticated()
                .anyRequest()
                .permitAll()
        // TODO : 베타 테스트 기간에만 permitAll -> 로그인 기능 추가되면 authenticated()로 변경
        );

    OAuth2AuthenticationFilter oAuth2AuthenticationFilter =
        new OAuth2AuthenticationFilter(
            createJWTAccessTokenService,
            createJWTRefreshTokenService,
            JWTRefreshTokenToCookieService,
            createOAuth2TokenService,
            deleteOAuth2TokenService);
    oAuth2AuthenticationFilter.setAuthenticationManager(authenticationManager);

    http.addFilterAt(oAuth2AuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    JWTReissueAuthenticationFilter jwtReissueAuthenticationFilter =
        new JWTReissueAuthenticationFilter(
            createJWTAccessTokenService,
            createJWTRefreshTokenService,
            JWTRefreshTokenRepository,
            saveJWTRefreshTokenService,
            JWTRefreshTokenToCookieService);
    ProviderManager jwtReissueAuthFilterProviderManager =
        new ProviderManager(new JWTReissueAuthenticationProvider(jwtUtil, userRepository));
    jwtReissueAuthenticationFilter.setAuthenticationManager(jwtReissueAuthFilterProviderManager);

    http.addFilterBefore(jwtReissueAuthenticationFilter, OAuth2AuthenticationFilter.class);

    JWTAuthenticationFilter jwtAuthenticationFilter =
        new JWTAuthenticationFilter(jwtUtil, userRepository);

    // TODO: 베타 테스트 이후 살려놓기
    //        http
    //                .addFilterAfter(jwtAuthenticationFilter, OAuth2AuthenticationFilter.class);

    http.logout(
        logout ->
            logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/logout", "POST"))
                .logoutSuccessHandler(
                    ((request, response, authentication) -> {
                      UserAccountInfoDTO userInfo =
                          ((OAuth2UserDetails) authentication.getPrincipal())
                              .getUserAccountInfoDTO();
                      deleteOAuth2TokenService.deleteByUserAccountInfoDTO(userInfo);
                    }))
                .deleteCookies("JSESSIONID", "refresh")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .addLogoutHandler(
                    ((request, response, authentication) -> {
                      Cookie[] cookies = request.getCookies();

                      for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("refresh")) {
                          String refresh = cookie.getValue();
                          JWTRefreshTokenRepository.deleteByRefreshToken(refresh);
                        }
                      }
                    })));

    http.sessionManagement(
        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable);

    http.cors(
        corsCustomizer ->
            corsCustomizer.configurationSource(
                new CorsConfigurationSource() {

                  @Override
                  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                  }
                }));

    return http.build();
  }
}
