package luv.dami.mobileauth.user;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  private String id;
  private String email;
  private String password;
  private AuthProvider provider;
  private String providerId;

  public static User createLocalUser(String email, String password) {
    return new User(email, password);
  }

  public static User createSocialUser(String email, AuthProvider provider, String providerId) {
    return new User(email, provider, providerId);
  }

  protected User() {
    this.id = UUID.randomUUID().toString();
  }

  private User(String email, String password) {
    this();
    this.email = email;
    this.password = password;
    this.provider = AuthProvider.LOCAL;
  }

  private User(String email, AuthProvider provider, String providerId) {
    this();
    this.email = email;
    this.provider = provider;
    this.providerId = providerId;
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public AuthProvider getProvider() {
    return provider;
  }

  public String getProviderId() {
    return providerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
