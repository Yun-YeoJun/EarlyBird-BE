package earlybird.earlybird.security.authentication.jwt.reissue;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTReissueAuthenticationToken extends AbstractAuthenticationToken {
  /** 인증 전: refresh 토큰 인증 성공 후: UserAccountInfoDTO 객체 */
  private final Object principal;

  public JWTReissueAuthenticationToken(
      Collection<? extends GrantedAuthority> authorities, Object principal) {
    super(authorities);
    this.principal = principal;
    setAuthenticated(true);
  }

  public JWTReissueAuthenticationToken(Object principal) {
    super(null);
    this.principal = principal;
    setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
