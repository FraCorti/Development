package it.colletta.security;

import static it.colletta.security.SecurityConstants.ACTIVATION_URL;
import static it.colletta.security.SecurityConstants.CHANGE_PASS_URL;
import static it.colletta.security.SecurityConstants.FORGOT_REQUEST_URL;
import static it.colletta.security.SecurityConstants.SIGN_UP_URL;

import com.google.common.collect.ImmutableList;

import it.colletta.service.user.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private BCryptPasswordEncoder passwordEncoder;

  /**
   * Constructor.
   *
   * @param userDetailsService User details
   * @param passwordEncoder Encoder
   */
  public WebSecurity(UserDetailsServiceImpl userDetailsService,
      BCryptPasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Configure the security for Spring.
   *
   * @param http the http security object
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().cors().and().authorizeRequests().antMatchers(HttpMethod.POST, SIGN_UP_URL)
        .permitAll().antMatchers(HttpMethod.GET, ACTIVATION_URL + "/**").permitAll()
        .antMatchers(HttpMethod.POST, FORGOT_REQUEST_URL + "/**").permitAll()
        .antMatchers(HttpMethod.POST, CHANGE_PASS_URL + "/**").permitAll()
        // .antMatchers(HttpMethod.GET, "/count").permitAll()
        .antMatchers("/*", "/resources/**", "/resources/static/**", "/resources/assets/**",
            "/resources/static/static/**")
        .anonymous().antMatchers(HttpMethod.POST, "/password/**").permitAll().anyRequest()
        .authenticated().and().addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService))
        // this disables session creation on Spring Security
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web)
      throws Exception {
    web.ignoring().antMatchers("/resources/**", "/static/**", "/assets/**",
        "/resources/static/static/**");
  }

  /**
   * Define the registerCorsConfiguration.
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(ImmutableList.of("*"));
    configuration.setAllowedMethods(
        ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(ImmutableList.of("*"));
    configuration.setExposedHeaders(ImmutableList.of("X-Auth-Token", "Authorization",
        "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
