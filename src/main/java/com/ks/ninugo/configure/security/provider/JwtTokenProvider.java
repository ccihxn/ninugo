package com.ks.ninugo.configure.security.provider;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final long tokenValidMillisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 여기에서 토큰 유효성을 검사하고 인증을 수행합니다.
        String token = (String) authentication.getCredentials();

        if (validationToken(token)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } else {
            return null; // 인증 실패
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    @PostConstruct
    protected void init() {
        logger.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        logger.info("[init] JwtTokenProvider 내 SecretKey 초기화 완료");
    }

    public String createToken(String userPk, String role) {
        logger.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
        logger.info("[createToken] 토큰 생성 완료");
        return token;
    }
    public Authentication getAuthentication(String token) {
        if (token == null) {
            logger.info("[getAuthentication] 토큰으로 인증 정보 조회 실패: 토큰이 null입니다.");
            return null;
        }

        logger.info("[getAuthentication] 토큰으로 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        logger.info("[getAuthentication] 토큰으로 인증 정보 조회 완료");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public String getUsername(String token) {
        if (token == null) {
            logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 실패: 토큰이 null입니다.");
            return null;
        }

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료");
        return info;
    }
    public String resolveToken(HttpServletRequest request){
        logger.info("[resolveToken] HTTP 요청 헤더에서 토큰 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }
    public boolean validationToken(String token){
        logger.info("[validationToken] 토큰 유효성 검증 시작");
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.SignatureException e) {
            logger.info("[validationToken] JWT 서명이 유효하지 않음");
            return false;
        } catch (Exception e){
            logger.info("[validationToken] 토큰 유효성 검증 실패");
            return false;
        }
    }
}
