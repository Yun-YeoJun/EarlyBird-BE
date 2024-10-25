package earlybird.earlybird.security.authentication.oauth2.proxy;

import earlybird.earlybird.security.authentication.oauth2.dto.GoogleServerResponse;
import earlybird.earlybird.security.authentication.oauth2.dto.OAuth2ServerResponse;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;


public class GoogleOAuth2UserInfoProxy implements OAuth2UserInfoProxy {
    @Override
    public OAuth2ServerResponse getOAuth2UserInfo(String accessToken) {

        String authorization = "Bearer " + accessToken;

        Mono<GoogleServerResponse> responseMono = WebClient.create("https://www.googleapis.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2/v2/userinfo")
                        .build(false))
                .header("Authorization", authorization)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new AuthenticationException(String.format("%s Error from google: %s", clientResponse.statusCode(), clientResponse.bodyToMono(String.class)))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new AuthenticationException(String.format("%s Error from google: %s", clientResponse.statusCode(), clientResponse.bodyToMono(String.class)))))
                .bodyToMono(GoogleServerResponse.class);

        return responseMono.block();
    }
}
