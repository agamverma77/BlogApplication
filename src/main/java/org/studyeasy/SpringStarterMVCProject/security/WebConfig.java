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
 
private static final String[] WHITELIST= {
    //we'll need to make a list of what all pages to allow
    //we need to add css all images files etc everything because spring security does not allow them also
 
"/",
 
"/login",
 
"/register",
 
"/db-console/**",
 
"/css/**", //this means allow all css files
 
"/fonts/**",
 
"/images/**",
 
"/js/**"
 
};

@Bean
public static PasswordEncoder passwordEncoder()
{
    return new BCryptPasswordEncoder();
}
 //chaining the configurations to http security object
@Bean
 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 
        http
 
            .authorizeHttpRequests((authz) -> {
                try {
                    authz

                                // .requestMatchers("/api/admin/**").hasRole("ADMIN")
 
                        
                                .requestMatchers(WHITELIST)

                                .permitAll()
                                .requestMatchers("/post/**").permitAll() // ✅ Add this line
                                .requestMatchers("/profile/**").authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/editor/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers("/test").hasAuthority(Privilages.ACCESS_ADMIN_PANEL.getAuthorityString())//earlier we used "/admin/** here also , but that called conflict & admin was not opening in super_admin, so to only show, i made test, will fix it later"
                                .anyRequest().authenticated() 
                                .and()
                                .formLogin(login -> {
                                    try {
                                        login
                                                        .loginPage("/login")
                                                        .loginProcessingUrl("/login")
                                                        .usernameParameter("email")
                                                        .passwordParameter("password")
                                                        .defaultSuccessUrl("/", true)//success login hone per redirect to home page
                                                        .failureUrl("/login?error")//failure login hone per redirect to login page with error message 
                                                        .permitAll()
                                                        .and()
                                                        .logout(logout -> logout
                                                                .logoutUrl("/logout")
                                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                                                                .logoutSuccessUrl("/"));
                                                        //.httpBasic(withDefaults());//this is the logout functionality that we'll use
                                                        //we are using Basic mechanism for login, we'll see more mechanisms when we study REST
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                        );
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
                    
                )

            //.httpBasic(Customizer.withDefaults())
            //remove these after upgrading the DB from H2 infile DB
            .csrf(csrf->csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/db-console/**")))
            .headers(headers->headers.frameOptions().disable());
        
        return http.build();
        //with theses configurations , we'll be able to allow those above defined pages
 
    }
/*private Customizer<HttpBasicConfigurer<HttpSecurity>> withDefaults() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'withDefaults'");
}*/
}