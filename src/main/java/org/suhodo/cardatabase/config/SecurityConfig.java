package org.suhodo.cardatabase.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.suhodo.cardatabase.filter.AuthenticationFilter;
import org.suhodo.cardatabase.service.AuthEntryPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static org.springframework.security.config.Customizer.withDefaults;

/*
 * 스프링 시큐리티 설정
 * 아래 어노테이션을 설정하면 기본 웹 보호 구성을 해제
 * -> 우리가 직접 설정한다.
 */
@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    private final AuthEntryPoint authEntryPoint;

    /*
     * 아래 코드는 직접 인증에 필요한 AuthenticationManagerBuilder를 설정하는 역할이지만
     * 현재는 직접 등록하지 않아도 됨.
     */
    // 시큐리티 인증과정에 필요한 AuthenticationManagerBuilder 객체에
    // DB의 사용자 정보를 반환하는 역할을 하는 userDetailsService를 등록
    // 패스워드 암호화 시 BCryptPasswordEncoder를 사용하겠다.
    /*
     * private final UserDetailsServiceImpl userDetailsService;
     * private final AuthenticationManagerBuilder auth;
     * 
     * // 생성자 호출 이후에 자동 실행(초기화)
     * 
     * @PostConstruct
     * public void configureGlobal() throws Exception {
     * log.info("configureGlobal................");
     * 
     * auth.userDetailsService(userDetailsService)
     * .passwordEncoder(new BCryptPasswordEncoder());
     * }
     */

    // BCrypt 해싱 알고리즘 객체(password -> hashcode로 변환)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // LoginController에서 사용할 AuthenticationManager객체를 Bean으로 등록
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }



    // 스프링 시큐리티에서 어떤 경로는 보호/비보호 결정
    // 보안 적용/비적용 결정
    /*
     * CSRF는 Session을 사용하는데, 우리는 Ajax json STATELESS 통신이므로, Session관리가 없다.
     * 불필요해서 disable
     */
    /* /login 주소 요청은 허용한다. */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("filterChain.................");

        http.csrf((csrf) -> csrf.disable())
            .cors(withDefaults())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost", "http://localhost:9999", "http://192.168.56.102:9999/")); // 허용 주소 추가
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                config.setAllowedOriginPatterns(List.of("http://*", "https://*"));    
                return config;
            }))
            .sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers("/").permitAll() // 루트 경로 허용
                .requestMatchers("/**").permitAll() // 모든 경로 허용 (http://localhost:9999 포함)
                .anyRequest().authenticated())
            .addFilterBefore(authenticationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authEntryPoint));

        return http.build();
    }




    // 임시로 React App이 자유롭게 요청하도록 설정을 변경한다.
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // log.info("filterChain.................");

    // // 인증없이 모두 허용
    // http.csrf(csrf -> csrf.disable())
    // .cors(cors -> cors.configurationSource(request -> {
    // CorsConfiguration config = new CorsConfiguration();
    // config.setAllowedOrigins(List.of("http://localhost:3000")); // 리액트 앱 주소
    // config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    // config.setAllowedHeaders(List.of("*"));
    // config.setAllowCredentials(true);
    // return config;
    // }))
    // .authorizeHttpRequests(auth -> auth
    // .anyRequest().permitAll());

    // return http.build();
    // }

    /*
     * CORS(Cross-Origin Resource Sharing, 교차 출처 리소스 공유)는
     * 웹 브라우저에서 한 출처(origin)의 웹 페이지가 다른 출처의 리소스에 접근할 수 있도록 허용하거나 제한하는 보안 기능입니다.
     * 
     * 주요 내용:
     * 
     * 기본적으로 브라우저는 보안상의 이유로, 다른 출처(도메인, 프로토콜, 포트가 다름)의 리소스 요청을 제한합니다.
     * CORS는 서버가 특정 출처에서 오는 요청을 허용하도록 HTTP 헤더(Access-Control-Allow-Origin 등)를 통해
     * 명시적으로 허용할 수 있게 해줍니다.
     * 예를 들어, http://localhost:3000에서 실행 중인 프론트엔드가 http://localhost:8080의 백엔드 API에
     * 요청할 때, 백엔드 서버가 CORS를 허용하지 않으면 브라우저가 요청을 차단합니다.
     * 서버에서 CORS 정책을 설정하면, 허용된 출처에서의 요청만 정상적으로 처리됩니다.
     * 정리:
     * CORS는 웹 애플리케이션의 보안을 강화하면서, 필요한 경우 다른 출처 간의 안전한 데이터 교환을 가능하게 해주는 표준입니다.
     */
    // 클래스 내에 전역 CORS 필터 추가
    @Bean
    public CorsConfigurationSource corsConfigureationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
