package earlybird.earlybird.security.authentication.oauth2.dto;

/** OAuth2 서버(ex. Google, Apple ...)의 응답을 담은 객체 */
public interface OAuth2ServerResponse {

  // 제공자 (Ex. naver, google, ...)
  String getProviderName();

  // 제공자에서 발급해주는 아이디(번호)
  String getProviderId();

  // 이메일
  String getEmail();

  // 사용자 실명 (설정한 이름)
  String getName();
}
