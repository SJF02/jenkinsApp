package org.suhodo.cardatabase.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.suhodo.cardatabase.domain.AccountCredentials;
import org.suhodo.cardatabase.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class LoginController {

    static final String PREFIX = "Bearer";

    private final JwtService jwtService;                        // Jwt 발급/검증
    private final AuthenticationManager authenticationManager;  // Spring Security인증

    // React앱에서 전송하는 json(username, password)데이터를 credentials에 저장
    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials){

        log.info("login : " + credentials + " ..............");

        UsernamePasswordAuthenticationToken creds = 
            new UsernamePasswordAuthenticationToken(credentials.username(), 
                                                    credentials.password());

        // Spring Security 내부 프로세스에 의해서 인증이 이루어짐
        Authentication auth = authenticationManager.authenticate(creds);

        // username으로 토큰 발급
        String jwts = jwtService.getToken(auth.getName());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, PREFIX + jwts)
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
            .build();
    }
}
