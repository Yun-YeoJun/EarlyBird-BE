package earlybird.earlybird.security.authentication.jwt.reissue;

import earlybird.earlybird.security.token.jwt.JWTUtil;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JWTReissueAuthenticationProvider implements AuthenticationProvider {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String refreshToken = (String) authentication.getPrincipal();

        if (refreshToken == null) {
            throw new AuthenticationServiceException("refresh 토큰이 null 입니다.");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationServiceException("refresh 토큰이 만료되었습니다.");
        }

        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {
            throw new AuthenticationServiceException("주어진 토큰이 refresh 토큰이 아닙니다.");
        }

        String accountId = jwtUtil.getAccountId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("refresh 토큰에 해당하는 사용자를 찾을 수 없습니다");
        }

        User user = optionalUser.get();

        if (!user.getRole().equals(role)) {
            throw new AuthenticationServiceException("refresh 토큰의 role과 저장된 role이 다릅니다.");
        }

        UserAccountInfoDTO userAccountInfoDTO = user.toUserAccountInfoDTO();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new JWTReissueAuthenticationToken(authorities, userAccountInfoDTO);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JWTReissueAuthenticationToken.class);
    }
}
