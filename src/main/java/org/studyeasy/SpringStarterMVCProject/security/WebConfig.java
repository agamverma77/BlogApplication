package org.studyeasy.SpringStarterMVCProject.security;

import org.springframework.context.annotation.Bean;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.studyeasy.SpringStarterMVCProject.util.constants.Privilages;
import org.studyeasy.SpringStarterMVCProject.util.constants.Roles;

import static org.springframework.security.config.Customizer.withDefaults;

 
 
@Configuration
@EnableMethodSecurity
@SuppressWarnings("removal")
public class WebConfig {

    private static final String[] WHITELIST = {
        "/",
        "/login",
        "/register",
        "/db-console/**",
        "/css/**",
        "/fonts/**",
        "/images/**",
        "/js/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> {
                try {
                    authz
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers("/post/**").permitAll()
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/editor/**").hasAnyRole("ADMIN", "EDITOR")
                        .requestMatchers("/test").hasAuthority(Privilages.ACCESS_ADMIN_PANEL.getAuthorityString())
                        .anyRequest().authenticated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
            .formLogin(login -> {
                try {
                    login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/"))
            .rememberMe(rememberMe -> rememberMe
                .rememberMeParameter("remember-me")
                .key("mySecureKey123") // Optional: Set a secure unique key
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/db-console/**"))
            )
            .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
}
