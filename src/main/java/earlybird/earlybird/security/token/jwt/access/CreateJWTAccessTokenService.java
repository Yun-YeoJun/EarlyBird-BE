package earlybird.earlybird.security.token.jwt.access;

import earlybird.earlybird.security.token.jwt.JWTUtil;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateJWTAccessTokenService {

    private final JWTUtil jwtUtil;

    public String createAccessToken(UserAccountInfoDTO userDTO, final Long expiredMs) {
        String accountId = userDTO.getAccountId();
        String role = userDTO.getRole();

        return jwtUtil.createJwt("access", accountId, role, expiredMs);
    }
}
