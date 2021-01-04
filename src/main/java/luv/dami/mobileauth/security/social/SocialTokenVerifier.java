package luv.dami.mobileauth.security.social;

import org.springframework.security.authentication.AuthenticationServiceException;

public interface SocialTokenVerifier {
  SocialUser verify(SocialAuthenticationToken authentication) throws AuthenticationServiceException;
  boolean supports(SocialProvider provider);
}
