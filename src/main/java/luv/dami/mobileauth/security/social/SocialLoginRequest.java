package luv.dami.mobileauth.security.social;

public class SocialLoginRequest {
  private SocialProvider provider;
  private String token;

  public SocialProvider getProvider() {
    return provider;
  }

  public void setProvider(SocialProvider provider) {
    this.provider = provider;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
