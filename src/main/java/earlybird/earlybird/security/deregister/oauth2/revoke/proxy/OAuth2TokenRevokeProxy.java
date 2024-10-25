package earlybird.earlybird.security.deregister.oauth2.revoke.proxy;

public interface OAuth2TokenRevokeProxy {

    /**
     * OAuth2 액세스 토큰 또는 리프레시 토큰을 만료시킴
     * @param token OAuth2 액세스 토큰 또는 리프레시 토큰
     */
    public void revoke(String token);
}
