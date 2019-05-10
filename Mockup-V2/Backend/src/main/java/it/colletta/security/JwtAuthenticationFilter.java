package it.colletta.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static it.colletta.security.SecurityConstants.EXPIRATION_TIME;
import static it.colletta.security.SecurityConstants.HEADER_STRING;
import static it.colletta.security.SecurityConstants.SECRET;
import static it.colletta.security.SecurityConstants.TOKEN_PREFIX;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.colletta.model.UserModel;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  /**
   * Costructor to filter security class.
   *
   * @param authenticationManager the authentication custom manager
   * @since 1.0
   */
  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * Perform authentication.
   *
   * @param request the HTTP request from the server
   * @param response the HTTP response
   * @return Authentication the authentication manager
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      UserModel creds = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
      return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          creds.getUsername().toLowerCase(), creds.getPassword(), new ArrayList<>()));
    } catch (IOException error) {
      throw new RuntimeException(error);
    }
  }

  /**
   * Menage the creation of JWT token and adds it to the response.
   *
   * @param request the HTTP request from the client
   * @param response the HTTP reponse with token generated
   * @param auth the auth provider from Spring
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication auth) throws IOException, ServletException {
    UserModel userModel = ((UserModel) auth.getPrincipal());
    userModel.setPassword(null);
    String token =
        JWT.create().withJWTId(userModel.getId()).withSubject(userModel.getUsername().toLowerCase())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    response.setHeader("Access-Control-Expose-Headers", "Authorization");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write((new ObjectMapper()).writeValueAsString(userModel));
    response.getWriter().flush();
    response.getWriter().close();
  }
}
