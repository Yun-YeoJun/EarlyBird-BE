package earlybird.earlybird.security.config;

import earlybird.earlybird.security.authentication.jwt.JWTAuthenticationFilter;
import earlybird.earlybird.security.authentication.jwt.reissue.JWTReissueAuthenticationFilter;
import earlybird.earlybird.security.authentication.jwt.reissue.JWTReissueAuthenticationProvider;
import earlybird.earlybird.security.authentication.oauth2.OAuth2AuthenticationFilter;
import earlybird.earlybird.security.authentication.oauth2.OAuth2AuthenticationProvider;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserJoinService;
import earlybird.earlybird.security.jwt.JWTUtil;
import earlybird.earlybird.security.jwt.access.CreateAccessTokenService;
import earlybird.earlybird.security.jwt.refresh.CreateRefreshTokenService;
import earlybird.earlybird.security.jwt.refresh.RefreshTokenRepository;
import earlybird.earlybird.security.jwt.refresh.RefreshTokenToCookieService;
import earlybird.earlybird.security.jwt.refresh.SaveRefreshTokenService;
import earlybird.earlybird.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;
    private final OAuth2UserJoinService oAuth2UserJoinService;
    private final CreateAccessTokenService createAccessTokenService;
    private final CreateRefreshTokenService createRefreshTokenService;
    private final RefreshTokenToCookieService refreshTokenToCookieService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final SaveRefreshTokenService saveRefreshTokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new OAuth2AuthenticationProvider(userDetailsService, oAuth2UserJoinService));
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .authenticationManager(authenticationManager);


        http
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                        // TODO : 베타 테스트 기간에만 permitAll -> 로그인 기능 추가되면 authenticated()로 변경
                );

        OAuth2AuthenticationFilter oAuth2AuthenticationFilter
                = new OAuth2AuthenticationFilter(createAccessTokenService, createRefreshTokenService, refreshTokenToCookieService);
        oAuth2AuthenticationFilter.setAuthenticationManager(authenticationManager);

        http
                .addFilterAt(oAuth2AuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        JWTReissueAuthenticationFilter jwtReissueAuthenticationFilter
                = new JWTReissueAuthenticationFilter(createAccessTokenService, createRefreshTokenService, refreshTokenRepository, saveRefreshTokenService, refreshTokenToCookieService);
        ProviderManager jwtReissueAuthFilterProviderManager = new ProviderManager(new JWTReissueAuthenticationProvider(jwtUtil, userRepository));
        jwtReissueAuthenticationFilter.setAuthenticationManager(jwtReissueAuthFilterProviderManager);

        http.addFilterBefore(jwtReissueAuthenticationFilter, OAuth2AuthenticationFilter.class);

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtUtil, userRepository);

        // TODO: 베타 테스트 이후 살려놓기
//        http
//                .addFilterAfter(jwtAuthenticationFilter, OAuth2AuthenticationFilter.class);

        http
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/logout", "POST"))
                        .logoutSuccessHandler(((request, response, authentication) -> {

                        }))
                        .deleteCookies("JSESSIONID", "refresh")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler(((request, response, authentication) -> {
                            Cookie[] cookies = request.getCookies();

                            for (Cookie cookie : cookies) {
                                if (cookie.getName().equals("refresh")) {
                                    String refresh = cookie.getValue();
                                    refreshTokenRepository.deleteByRefreshToken(refresh);
                                }
                            }
                        }))
                );

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

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
