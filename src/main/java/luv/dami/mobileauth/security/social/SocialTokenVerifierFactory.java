package luv.dami.mobileauth.security.social;

import java.util.List;

public class SocialTokenVerifierFactory {

  private final List<SocialTokenVerifier> verifiers;

  public SocialTokenVerifierFactory(List<SocialTokenVerifier> verifiers) {
    this.verifiers = verifiers;
  }

  public SocialTokenVerifier getVerifier(SocialProvider provider) {
    return verifiers.stream()
        .filter(verifier -> verifier.supports(provider))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("Unexpected provider:" + provider.name()));
  }
}
