package earlybird.earlybird.security.deregister.oauth2.revoke;

import earlybird.earlybird.security.deregister.oauth2.revoke.proxy.OAuth2TokenRevokeProxy;
import earlybird.earlybird.security.enums.OAuth2ProviderName;
import earlybird.earlybird.security.token.oauth2.OAuth2TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class RevokeOAuth2TokenService {

    private final Map<OAuth2ProviderName, OAuth2TokenRevokeProxy> oAuth2TokenRevokeProxyMap;

    public void revoke(OAuth2TokenDTO oAuth2TokenDTO) {
        OAuth2ProviderName oAuth2ProviderName = oAuth2TokenDTO.getOAuth2ProviderName();
        OAuth2TokenRevokeProxy oAuth2TokenRevokeProxy = oAuth2TokenRevokeProxyMap.get(oAuth2ProviderName);

        String accessToken = oAuth2TokenDTO.getAccessToken();
        String refreshToken = oAuth2TokenDTO.getRefreshToken();

        oAuth2TokenRevokeProxy.revoke(accessToken);
        oAuth2TokenRevokeProxy.revoke(refreshToken);
    }
}
