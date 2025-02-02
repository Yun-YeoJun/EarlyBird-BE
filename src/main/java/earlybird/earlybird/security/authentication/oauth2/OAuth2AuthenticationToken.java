package earlybird.earlybird.security.authentication.oauth2;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OAuth2AuthenticationToken extends AbstractAuthenticationToken {

  /**
   * 인증 전: provider 이름 (String)<br>
   * 인증 성공 후: UserDetails 객체
   */
  private final Object principal;

  /**
   * 인증 전: OAuth2 액세스 토큰 (String)<br>
   * 인증 성공 후: null
   */
  private final Object credentials;

  public OAuth2AuthenticationToken(
      Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(true);
  }

  /**
   * @param principal provider 이름
   * @param credentials OAuth2 액세스 토큰
   */
  public OAuth2AuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
