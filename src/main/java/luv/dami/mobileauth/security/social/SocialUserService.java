package luv.dami.mobileauth.security.social;

import java.util.Optional;
import luv.dami.mobileauth.security.UserPrincipal;
import luv.dami.mobileauth.user.AuthProvider;
import luv.dami.mobileauth.user.User;
import luv.dami.mobileauth.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class SocialUserService {

  private final UserRepository userRepository;

  public SocialUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails loadUser(SocialUser socialUser) {
    if (!StringUtils.hasLength(socialUser.getEmail())) {
      throw new SocialAuthenticationProcessingException("Email not found from social provider");
    }

    Optional<User> userOptional = userRepository.findByEmail(socialUser.getEmail());
    User user;
    if (userOptional.isPresent()) {
      user = userOptional.get();
      if (!user.getProvider().name().equals(socialUser.getProvider().name())) {
        throw new SocialAuthenticationProcessingException("Look like you're signed up with " +
            user.getProvider() + " account. Please user your " + user.getProvider() +
            "account to login.");
      }

      user = updateExistingUser(user, socialUser);
    } else {
      user = registerNewUser(socialUser);
    }

    return UserPrincipal.create(user);
  }

  private User registerNewUser(SocialUser socialUser) {
    return userRepository.save(
        User.createSocialUser(
            socialUser.getEmail(),
            AuthProvider.valueOf(socialUser.getProvider().name()),
            socialUser.getProviderId()));
  }

  private User updateExistingUser(User existingUser, SocialUser socialUser) {
    // Update an user if necessary.

    return userRepository.save(existingUser);
  }
}
