package earlybird.earlybird.security.token.jwt.refresh;

import earlybird.earlybird.security.token.jwt.JWTUtil;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateJWTRefreshTokenService {

    private final JWTUtil jwtUtil;
    private final SaveJWTRefreshTokenService saveJWTRefreshTokenService;

    public String createRefreshToken(UserAccountInfoDTO userDTO, final Long expiredMs) {
        String accountId = userDTO.getAccountId();
        String role = userDTO.getRole();

        String refresh = jwtUtil.createJwt("refresh", accountId, role, expiredMs);

        saveJWTRefreshTokenService.saveJWTRefreshToken(accountId, refresh, expiredMs);

        return refresh;
    }
}
