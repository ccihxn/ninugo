package com.ks.ninugo.configure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ks.ninugo.configure.security.provider.JwtTokenProvider;
import com.ks.ninugo.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

        setFilterProcessesUrl("/user/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[attemptAuthentication] 인증 시도");
        ObjectMapper om = new ObjectMapper();
        UserDTO userDTO = null;

        try {
            userDTO = om.readValue(request.getInputStream(), UserDTO.class);
        }catch (Exception e) {
            throw new BadCredentialsException("인증 정보를 읽어올 수 없습니다.", e);
        }
        log.info("[attemptAuthentication] 인증 시도 유저 정보: {}", userDTO);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getLoginId(), userDTO.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }
    // success 시 호출

}
