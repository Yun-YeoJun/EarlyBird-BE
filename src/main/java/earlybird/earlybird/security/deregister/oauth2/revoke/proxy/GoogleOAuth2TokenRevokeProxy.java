package earlybird.earlybird.security.deregister.oauth2.revoke.proxy;

import earlybird.earlybird.security.authentication.oauth2.dto.GoogleServerResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;

@Component
public class GoogleOAuth2TokenRevokeProxy implements OAuth2TokenRevokeProxy {
    @Override
    public void revoke(String token) {
        try {
            WebClient.create("https://oauth2.googleapis.com")
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path("/revoke")
                            .queryParam("token", token)
                            .build(false))
                    .header("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException()))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException()))
                    .bodyToMono(String.class)
                    .block();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
