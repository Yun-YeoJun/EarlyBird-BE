package earlybird.earlybird.security.deregister.oauth2;

import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.security.deregister.oauth2.revoke.RevokeOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import earlybird.earlybird.security.token.oauth2.service.DeleteOAuth2TokenService;
import earlybird.earlybird.security.token.oauth2.service.FindOAuth2TokenService;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.service.DeleteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2DeregisterService {

    private final DeleteUserService deleteUserService;
    private final FindOAuth2TokenService findOAuth2TokenService;
    private final RevokeOAuth2TokenService revokeOAuth2TokenService;
    private final DeleteOAuth2TokenService deleteOAuth2TokenService;

    public void deregister(OAuth2UserDetails userDetails) {
        UserAccountInfoDTO userInfo = userDetails.getUserAccountInfoDTO();
        Long userId = userInfo.getId();
        OAuth2TokenDTO oAuth2TokenDTO = findOAuth2TokenService.findByUserId(userId);

        revokeOAuth2TokenService.revoke(oAuth2TokenDTO);
        deleteOAuth2TokenService.deleteByUserAccountInfoDTO(userInfo);
        deleteUserService.deleteUser(userInfo);
    }
}
