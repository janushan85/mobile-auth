package luv.dami.mobileauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final TokenProvider tokenProvider;

  public CustomAuthenticationSuccessHandler(ObjectMapper objectMapper,
      TokenProvider tokenProvider) {
    this.objectMapper = objectMapper;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    String token = tokenProvider.createToken(authentication);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    try (PrintWriter writer = response.getWriter()) {
      writer.write(
          objectMapper.writeValueAsString(
              Collections.singletonMap("accessToken", token)));
      writer.flush();
    }
  }
}
