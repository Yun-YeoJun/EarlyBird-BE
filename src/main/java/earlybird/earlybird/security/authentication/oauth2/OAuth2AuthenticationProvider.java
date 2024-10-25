package earlybird.earlybird.security.authentication.oauth2;

import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;
import earlybird.earlybird.security.authentication.oauth2.proxy.GoogleOAuth2UserInfoProxy;
import earlybird.earlybird.security.authentication.oauth2.proxy.OAuth2UserInfoProxy;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserJoinService;
import earlybird.earlybird.security.enums.OAuth2ProviderName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

@RequiredArgsConstructor
public class OAuth2AuthenticationProvider implements AuthenticationProvider {

    private final static Map<OAuth2ProviderName, OAuth2UserInfoProxy> oauth2UserInfoProxyList
            = Map.of(OAuth2ProviderName.GOOGLE, new GoogleOAuth2UserInfoProxy());

    private final UserDetailsService userDetailsService;
    private final OAuth2UserJoinService oAuth2UserJoinService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String oauth2ProviderName = ((String) authentication.getPrincipal()).toLowerCase();
        String oauth2AccessToken = (String) authentication.getCredentials();


        for (OAuth2ProviderName oAuth2ProviderName : oauth2UserInfoProxyList.keySet()) {
            String proxyName = oAuth2ProviderName.name();
            if (proxyName.equalsIgnoreCase(oauth2ProviderName)) {
                OAuth2UserInfoProxy oAuth2UserInfoProxy = oauth2UserInfoProxyList.get(oAuth2ProviderName);
                OAuth2ServerResponse oAuth2UserInfo = oAuth2UserInfoProxy.getOAuth2UserInfo(oauth2AccessToken);

                String username = oAuth2UserInfo.getProviderName() + " " + oAuth2UserInfo.getProviderId();

                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e) {
                    oAuth2UserJoinService.join(oAuth2UserInfo);
                    userDetails = userDetailsService.loadUserByUsername(username);
                }

                return new OAuth2AuthenticationToken(userDetails.getAuthorities(), userDetails, null);
            }
        }

        throw new AuthenticationServiceException("지원하지 않는 OAuth2 provider입니다.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OAuth2AuthenticationToken.class);
    }
}
