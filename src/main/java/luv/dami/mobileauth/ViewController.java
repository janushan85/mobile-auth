package luv.dami.mobileauth;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

  @Value("${signin.google.clientId}")
  private String googleClientId;

  @Value("${signin.google.scope}")
  private String googleScope;

  @Value("${signin.apple.clientId}")
  private String appleClientId;

  @Value("${signin.apple.scope}")
  private String appleScope;

  @Value("${signin.apple.redirectUri}")
  private String appleRedirectUri;

  @Value("${signin.facebook.clientId}")
  private String facebookClientId;

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/login")
  public String login(Model model) {
    model.addAttribute("googleClientId", googleClientId);
    model.addAttribute("googleScope", googleScope);
    model.addAttribute("appleClientId", appleClientId);
    model.addAttribute("appleScope", appleClientId);
    model.addAttribute("appleRedirectUri", appleRedirectUri);
    model.addAttribute("appleState", UUID.randomUUID().toString());
    model.addAttribute("appleNonce", UUID.randomUUID().toString());
    model.addAttribute("facebookClientId", facebookClientId);

    return "login";
  }

  @RequestMapping("/registration")
  public String registration() {
    return "registration";
  }
}
