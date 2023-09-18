package com.ks.ninugo.configure.security;

import com.ks.ninugo.dto.UserDTO;
import com.ks.ninugo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        log.info("email :: {}",loginId);

        UserDTO user = userService.findUserByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        log.info("LOAD USER BY USERNAME = USER : {}, {}",user.getLoginId(), user.getPassword());

        return new PrincipalDetails(user);
    }

}