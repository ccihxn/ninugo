package com.ks.ninugo.configure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ks.ninugo.configure.security.PrincipalDetails;
import com.ks.ninugo.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import com.ks.ninugo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@PropertySource("classpath:application-jwt.properties") // 프로퍼티 파일 경로 지정
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final long VALID_TIME = 1000L * 60 * 60; // 1시간만 토큰 유효
    @Value("${spring.jwt.secret}")
    private String secretKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("--JWT AUTHENTICATION FILTER--");

        try {
            UserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getLoginId(),
                            userDTO.getPassword(),
                            null
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userId = ((PrincipalDetails) authResult.getPrincipal()).getUsername();
        Optional<UserDTO> userResponseOptional = userService.findUserByLoginId(userId);

        if (userResponseOptional.isPresent()) { // Optional이 비어있지 않을 때만 처리
            UserDTO userResponse = userResponseOptional.get();

            String jwtToken = Jwts.builder()
                    .setSubject(userResponse.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + VALID_TIME))
                    .signWith(SignatureAlgorithm.HS256, getSecretKey())
                    .compact();

            response.addHeader("token", jwtToken);
            response.addHeader("username", userResponse.getLoginId());
        }
    }

    private Key getSecretKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
