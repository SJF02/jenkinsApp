package org.suhodo.cardatabase.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.suhodo.cardatabase.domain.AppUser;
import org.suhodo.cardatabase.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final AppUserRepository repository;

    // 스프링 시큐리티 인증 과정에서 아래 함수가 반드시 호출된다.
    // 아래에서 DB의 사용자 정보가 있는 지 확인해서 
    // 인증정보를 UserBuilder 객체에 담아서 리턴한다.
    // 리턴한 객체는 인증과정에 참여하게 된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername : " + username + " ................");

        Optional<AppUser> user = repository.findByUsername(username);

        UserBuilder builder = null;
        if(user.isPresent()){       // 일치하는 사용자 정보가 DB에 존재한다면
            AppUser currentUser = user.get();
            log.info("loadUserByUsername : AppUser = " + currentUser);
            builder = User.withUsername(username);
            builder.password(currentUser.getPassword());
            builder.roles(currentUser.getRole());
        }else{                      // 일치하는 사용자 정보가 DB에 없다면
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }
}
