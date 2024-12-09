package net.devgrr.springdemo.login.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

public class JsonUsernamePasswordAuthenticationFilter
    extends AbstractAuthenticationProcessingFilter {

  private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
  private static final String HTTP_METHOD = "POST";
  private static final String CONTENT_TYPE = "application/json";
  private static final String USERID_KEY = "userId";
  private static final String PASSWORD_KEY = "password";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL); // , HTTP_METHOD);
  private final ObjectMapper objectMapper;

  public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    if (!HTTP_METHOD.equals(request.getMethod())) {
      throw new AuthenticationServiceException(
          "Authentication Method not supported: " + request.getMethod());
    }

    if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
      throw new AuthenticationServiceException(
          "Authentication Content-Type not supported: " + request.getContentType());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
    Map<String, String> userIdPasswordMap =
        objectMapper.readValue(messageBody, new TypeReference<>() {});
    UsernamePasswordAuthenticationToken authRequest =
        new UsernamePasswordAuthenticationToken(
            userIdPasswordMap.get(USERID_KEY), userIdPasswordMap.get(PASSWORD_KEY));

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}