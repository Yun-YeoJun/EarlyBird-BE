package earlybird.earlybird.security.deregister.oauth2;

import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.security.deregister.oauth2.revoke.RevokeOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.service.DeleteOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.service.FindOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.service.DeleteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OAuth2DeregisterController {

    private final OAuth2DeregisterService oAuth2DeregisterService;

    @DeleteMapping("/api/v1/users")
    public ResponseEntity<?> deregister(@AuthenticationPrincipal OAuth2UserDetails userDetails) {
        oAuth2DeregisterService.deregister(userDetails);
        return ResponseEntity.ok().build();
    }
}
