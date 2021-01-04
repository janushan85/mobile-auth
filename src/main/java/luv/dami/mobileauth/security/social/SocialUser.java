package luv.dami.mobileauth.security.social;

public class SocialUser {

  private String email;
  private SocialProvider provider;
  private String providerId;

  public SocialUser(String email, SocialProvider provider, String providerId) {
    this.email = email;
    this.provider = provider;
    this.providerId = providerId;
  }

  public String getEmail() {
    return email;
  }

  public SocialProvider getProvider() {
    return provider;
  }

  public String getProviderId() {
    return providerId;
  }
}
