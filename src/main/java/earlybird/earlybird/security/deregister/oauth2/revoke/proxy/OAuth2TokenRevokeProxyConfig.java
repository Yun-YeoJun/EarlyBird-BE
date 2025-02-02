package earlybird.earlybird.security.deregister.oauth2.revoke.proxy;

import earlybird.earlybird.security.enums.OAuth2ProviderName;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2TokenRevokeProxyConfig {

  @Bean
  public Map<OAuth2ProviderName, OAuth2TokenRevokeProxy> providerNameAndProxyMap(
      GoogleOAuth2TokenRevokeProxy googleOAuth2TokenRevokeProxy) {
    return Map.of(OAuth2ProviderName.GOOGLE, googleOAuth2TokenRevokeProxy);
  }
}
