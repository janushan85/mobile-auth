package luv.dami.mobileauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import javax.servlet.Filter;
import luv.dami.mobileauth.security.CustomAuthenticationFailureHandler;
import luv.dami.mobileauth.security.CustomAuthenticationSuccessHandler;
import luv.dami.mobileauth.security.CustomUserDetailsService;
import luv.dami.mobileauth.security.TokenAuthenticationFilter;
import luv.dami.mobileauth.security.social.AppleTokenVerifier;
import luv.dami.mobileauth.security.social.FacebookTokenVerifier;
import luv.dami.mobileauth.security.social.GoogleTokenVerifier;
import luv.dami.mobileauth.security.social.SocialAuthenticationFilter;
import luv.dami.mobileauth.security.social.SocialAuthenticationProvider;
import luv.dami.mobileauth.security.TokenProvider;
import luv.dami.mobileauth.security.social.SocialTokenVerifierFactory;
import luv.dami.mobileauth.security.social.SocialUserService;
import luv.dami.mobileauth.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;

  public SecurityConfig(ObjectMapper objectMapper,
      UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.userRepository = userRepository;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/h2-console/**")
        .antMatchers("/login/local")
        .antMatchers("/login/social");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/registration", "/login", "/").permitAll()
        .anyRequest().authenticated()
        .and()
        .cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .addFilterBefore(socialAuthenticationFilter(), FilterSecurityInterceptor.class)
        .addFilterBefore(localAuthenticationFilter(), FilterSecurityInterceptor.class)
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean Filter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter(tokenProvider(), userRepository);
  }

  @Bean
  public Filter socialAuthenticationFilter() {
    SocialAuthenticationFilter filter = new SocialAuthenticationFilter("/login/social");
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(authenticationFailureHandler());

    return filter;
  }

  @Bean
  public Filter localAuthenticationFilter() {
    UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(
        new ProviderManager(localAuthenticationProvider()));
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(authenticationFailureHandler());

    return filter;
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler(objectMapper, tokenProvider());
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(
        Arrays.asList(localAuthenticationProvider(), socialAuthenticationProvider()));
  }

  @Bean
  public AuthenticationProvider socialAuthenticationProvider() {
    return new SocialAuthenticationProvider(socialUserService(), socialTokenVerifierFactory());
  }

  @Bean
  public AuthenticationProvider localAuthenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }

  @Bean
  public SocialTokenVerifierFactory socialTokenVerifierFactory() {
    return new SocialTokenVerifierFactory(
        Arrays.asList(
            new GoogleTokenVerifier(),
            new FacebookTokenVerifier(restTemplate()),
            new AppleTokenVerifier(restTemplate())));
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService(userRepository);
  }

  @Bean
  public SocialUserService socialUserService() {
    return new SocialUserService(userRepository);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
