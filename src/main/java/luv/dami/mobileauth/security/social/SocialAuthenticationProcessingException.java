package luv.dami.mobileauth.security.social;

public class SocialAuthenticationProcessingException extends RuntimeException {

  public SocialAuthenticationProcessingException(String message) {
    super(message);
  }

  public SocialAuthenticationProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
