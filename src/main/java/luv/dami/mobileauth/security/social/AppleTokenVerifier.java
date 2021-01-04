package luv.dami.mobileauth.security.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import luv.dami.mobileauth.security.social.AppleTokenVerifier.PublicKeys.Key;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.client.RestTemplate;

public class AppleTokenVerifier implements SocialTokenVerifier {

  private final RestTemplate restTemplate;

  public AppleTokenVerifier(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public SocialUser verify(SocialAuthenticationToken authentication) {
    PublicKeys publicKeys = getPublicKeys();
    Map<String, String> header = extractHeaderFromIdToken(authentication.getToken());
    Key key = publicKeys.getMatchedKeyBy(header.get("kid"), header.get("alg"))
        .orElseThrow(() -> new AuthenticationServiceException("Not found matched key."));

    Jws<Claims> claimsJws = Jwts.parser()
        .setSigningKey(generateSigningKey(key))
        .parseClaimsJws(authentication.getToken());

    // todo: verify the nonce for the authentication
    // todo: verify that the iss field contains https://appleid.apple.com
    // todo: verify that the aud field is the developer's client_id
    // todo: verify that the time is earlier than the exp value of the token

    return new SocialUser(String.valueOf(claimsJws.getBody().get("email")),
        authentication.getProvider(), claimsJws.getBody().getSubject());
  }

  @Override
  public boolean supports(SocialProvider provider) {
    return provider == SocialProvider.APPLE;
  }

  private PublicKey generateSigningKey(Key key) {
    try {
      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
          new BigInteger(1, Base64.getUrlDecoder().decode(key.getN())),
          new BigInteger(1, Base64.getUrlDecoder().decode(key.getE())));

      KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());

      return keyFactory.generatePublic(publicKeySpec);
    } catch (Exception e) {
     throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  private PublicKeys getPublicKeys() {
    return restTemplate.getForObject("https://appleid.apple.com/auth/keys", PublicKeys.class);
  }

  private Map extractHeaderFromIdToken(String idToken) {
    String header = idToken.substring(0, idToken.indexOf("."));
    try {
      return new ObjectMapper()
          .readValue(new String(Base64.getDecoder().decode(header), "UTF-8"), Map.class);
    } catch (Exception e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  static class PublicKeys {
    private List<Key> keys;

    public PublicKeys() {

    }

    public List<Key> getKeys() {
      return keys;
    }

    public void setKeys(
        List<Key> keys) {
      this.keys = keys;
    }

    public Optional<Key> getMatchedKeyBy(String kid, String alg) {
      return keys.stream()
          .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
          .findFirst();
    }

    static class Key {
      private String kty;
      private String kid;
      private String use;
      private String alg;
      private String n;
      private String e;

      public Key() {

      }

      public Key(String kty, String kid, String use, String alg, String n, String e) {
        this.kty = kty;
        this.kid = kid;
        this.use = use;
        this.alg = alg;
        this.n = n;
        this.e = e;
      }

      public String getKty() {
        return kty;
      }

      public String getKid() {
        return kid;
      }

      public String getUse() {
        return use;
      }

      public String getAlg() {
        return alg;
      }

      public String getN() {
        return n;
      }

      public String getE() {
        return e;
      }
    }
  }
}
