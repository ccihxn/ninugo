package com.ks.ninugo.configure.security.filter;

import com.ks.ninugo.configure.security.PrincipalDetails;
import com.ks.ninugo.configure.security.PrincipalDetailsService;
import com.ks.ninugo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private final PrincipalDetailsService principalDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, PrincipalDetailsService principalDetailsService) {
        super(authenticationManager);
        this.principalDetailsService = principalDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String tokenHeader = request.getHeader("Authorization");
            String jwtToken = extractTokenFromHeader(tokenHeader);

            if (jwtToken != null && isValid(jwtToken)) {
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtToken));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(String tokenHeader) {
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private Authentication getAuthentication(String jwtToken) {
        String email = getEmail(jwtToken);
        PrincipalDetails userDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String getEmail(String jwtToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.getSubject();
    }

    private boolean isValid(String jwtToken) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(jwtToken);

            return jws != null &&
                    jws.getBody().getSubject() != null &&
                    !jws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSecretKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
