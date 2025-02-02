package earlybird.earlybird.security.authentication.oauth2.proxy;

import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;

public interface OAuth2UserInfoProxy {

    /**
     * OAuth2 인증 서버에 액세스 토큰으로 요청을 보내서 유저 정보 획득 & 반환
     *
     * @param accessToken OAuth2 액세스 토큰
     * @return
     */
    public OAuth2ServerResponse getOAuth2UserInfo(String accessToken);
}
