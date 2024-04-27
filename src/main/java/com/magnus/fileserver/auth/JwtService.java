package com.magnus.fileserver.auth;

import com.magnus.fileserver.config.AppProperties;
import com.magnus.fileserver.user.User;
import com.magnus.fileserver.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final AppProperties appProperties;
  private final UserService userService;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public boolean isTokenValid(String token) {
    return !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date(System.currentTimeMillis()));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(getJwtSecret());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String getJwtSecret() {
    return appProperties.getJwtSecret();
  }

  public UsernamePasswordAuthenticationToken validateJWT(String jwt) {

    if (this.isTokenValid(jwt)) {
      Claims claims = this.extractAllClaims(jwt);

      User user = new User(
          claims.get("id", Long.class),
          claims.get("first_name", String.class),
          claims.get("last_name", String.class),
          claims.getSubject(),
          claims.get("username", String.class)
      );

      userService.updateOrCreateUser(user);

      return new UsernamePasswordAuthenticationToken(
          user,
          null,
          Collections.singleton((GrantedAuthority) () -> "USER")
      );
    }
    return null;
  }
}
