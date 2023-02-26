package com.example.demo.jwtutils;

import com.example.demo.exception.BearerMissingException;
import com.example.demo.response.GenericErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private static final List<String> UNAUTHORIZED_URIS = List.of("/healthcheck", "/login");
  private JwtUserDetailsService userDetailsService;
  private TokenManager tokenManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws IOException {

    String tokenHeader = request.getHeader("Authorization");
    String username = null;
    String token = null;

    try {
      if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
        token = tokenHeader.substring(7);
        username = tokenManager.getUsernameFromToken(token);
      } else {
        if (!UNAUTHORIZED_URIS.contains(request.getRequestURI())) {
          throw new BearerMissingException();
        }
      }

      if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (tokenManager.validateJwtToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken
              authenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, null,
              userDetails.getAuthorities());
          authenticationToken.setDetails(new
              WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (BearerMissingException e) {
      setErrorResponse(response, new BearerMissingException().getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) {
      setErrorResponse(response, "Unable to get JWT Token", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      setErrorResponse(response, "JWT Token has expired", HttpStatus.UNAUTHORIZED);
    }
  }

  private void setErrorResponse(HttpServletResponse response, String message, HttpStatus httpStatus) throws IOException {
    GenericErrorResponse errorResponse = new GenericErrorResponse(message);
    response.setStatus(httpStatus.value());
    response.getWriter().write(convertObjectToJson(errorResponse));
  }

  public String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }
}
