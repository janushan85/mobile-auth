package luv.dami.mobileauth.security.social;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SocialAuthenticationProvider implements AuthenticationProvider {

  private final SocialUserService socialUserService;
  private final SocialTokenVerifierFactory socialTokenVerifierFactory;

  public SocialAuthenticationProvider(SocialUserService socialUserService,
      SocialTokenVerifierFactory socialTokenVerifierFactory) {
    this.socialUserService = socialUserService;
    this.socialTokenVerifierFactory = socialTokenVerifierFactory;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    SocialAuthenticationToken socialAuthentication = (SocialAuthenticationToken) authentication;

    SocialUser socialUser = socialTokenVerifierFactory.getVerifier(socialAuthentication.getProvider())
            .verify(socialAuthentication);

    socialAuthentication.setPrincipal(
        socialUserService.loadUser(socialUser));

    return socialAuthentication;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (SocialAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
