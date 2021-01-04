package luv.dami.mobileauth.security.social;

import java.util.Map;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class FacebookTokenVerifier implements SocialTokenVerifier {

  private final RestTemplate restTemplate;

  public FacebookTokenVerifier(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public SocialUser verify(SocialAuthenticationToken authentication) {
    Map<String, String> result = restTemplate.getForObject(
        UriComponentsBuilder.fromUriString("https://graph.facebook.com/me")
            .queryParam("fields", "email,first_name,last_name")
            .queryParam("access_token", authentication.getToken())
            .build()
            .toUri()
        ,Map.class);

    return new SocialUser(result.get("email"), authentication.getProvider(), result.get("id"));
  }

  @Override
  public boolean supports(SocialProvider provider) {
    return provider == SocialProvider.FACEBOOK;
  }
}
