package luv.dami.mobileauth.security.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;

  public SocialAuthenticationFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
    objectMapper = new ObjectMapper();
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    SocialLoginRequest login;
    try {
      login = objectMapper.readValue(request.getReader(), SocialLoginRequest.class);
    } catch (Exception ex) {
      throw new AuthenticationServiceException("An error occurred while parsing the social login request,", ex);
    }

    SocialAuthenticationToken authentication =
        new SocialAuthenticationToken(
            Collections.singleton(new SimpleGrantedAuthority("USER")),
            login.getProvider(),
            login.getToken(),
            authenticationDetailsSource.buildDetails(request));

    return this.getAuthenticationManager().authenticate(authentication);
  }
}
