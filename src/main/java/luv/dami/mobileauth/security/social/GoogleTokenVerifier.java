package luv.dami.mobileauth.security.social;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Collections;
import org.springframework.security.authentication.AuthenticationServiceException;

public class GoogleTokenVerifier implements SocialTokenVerifier {

  @Override
  public SocialUser verify(SocialAuthenticationToken authentication) {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
        .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
        .setAudience(Collections.singletonList("534528486211-8f75rmsa9ojlg74f4badue52tv04hn8l.apps.googleusercontent.com"))
        .build();

    try {
      GoogleIdToken idToken = verifier.verify(authentication.getToken());

      return new SocialUser(
          idToken.getPayload().getEmail(),
          authentication.getProvider(),
          idToken.getPayload().getSubject());
    } catch (Exception e) {
      throw new AuthenticationServiceException("An error occurred while verifying the token for Google", e);
    }
  }

  @Override
  public boolean supports(SocialProvider provider) {
    return provider == SocialProvider.GOOGLE;
  }
}
