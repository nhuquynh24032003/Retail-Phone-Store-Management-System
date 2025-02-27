package com.example.ecommerce.config;

import com.example.ecommerce.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserRepository userRepository;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService(){
        return email-> userRepository.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Email not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/account", "/payment/**").authenticated()
                .requestMatchers("/Admin").hasRole("ADMIN")
            .anyRequest().permitAll()
            .and()
            .formLogin()
                .loginPage("/account/login").permitAll()
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {

                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    Cookie email = new Cookie("email", userDetails.getUsername());
                    email.setMaxAge(604800);
                    email.setPath("/");
                    response.addCookie(email);

                    if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                        response.sendRedirect("/Admin");
                    }
                    else{
                        response.sendRedirect("/");
                    }
                })
            .and()
            .oauth2Login()
                .loginPage("/account/login").permitAll()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(authSuccessHandler)
                .failureHandler(((request, response, exception) -> System.out.println("Failed")))
            .and()
            .logout()
                .logoutUrl("/account/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    Cookie email = new Cookie("email", null);
                    email.setMaxAge(0);
                    email.setPath("/");
                    response.addCookie(email);
                    response.sendRedirect("/");
                })
            .and()
            .rememberMe()
                .tokenValiditySeconds(2592000) // 30 days
                .key("uniqueRememberMeKey")
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

}
