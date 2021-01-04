package luv.dami.mobileauth.security.social;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SocialAuthenticationToken extends AbstractAuthenticationToken {

  private SocialProvider provider;
  private String token;
  private Object principal;

  public SocialAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
      SocialProvider provider, String token, Object details) {
    super(authorities);
    this.provider = provider;
    this.token = token;
    setDetails(details);
  }

  public SocialProvider getProvider() {
    return provider;
  }

  public String getToken() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }
}
