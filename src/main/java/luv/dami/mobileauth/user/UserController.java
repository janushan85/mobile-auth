package luv.dami.mobileauth.user;

import luv.dami.mobileauth.security.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserController(PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostMapping(path = "/registration", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void registration(UserRegistrationRequest request) {
    userRepository.save(
        User.createLocalUser(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())));
  }

  @GetMapping("/user")
  public UserPrincipal user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return userPrincipal;
  }
}
