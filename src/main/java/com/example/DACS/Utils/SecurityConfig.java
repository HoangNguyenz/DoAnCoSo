package com.example.DACS.Utils;


import com.example.DACS.services.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() { return new CustomUserDetailService();
    }
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                     .requestMatchers( "/css/**", "/js/**", "/", "/register", "/error", "/home","BookImage/**","/cart","/view/**","/cart", "/home/details/**", "/cart/**", "books/add-to-cart")
                     .permitAll()
                     .requestMatchers( "/books/edit", "/books/delete")
                                    .hasAuthority("ADMIN")
                     .requestMatchers("/books", "/books/add")
                                    .hasAnyAuthority("ADMIN")
                    .requestMatchers("/categories", "/categories/add", "categories/edit", "categories/delete")
                        .hasAnyAuthority("ADMIN")
                     .anyRequest().authenticated()
                 )
                .logout(logout -> logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID") //xóa cookies khi đăng xuất
                    .invalidateHttpSession(true) //Hủy bỏ phiên làm việc (HttpSession) khi đăng xuất.
                    .clearAuthentication(true) //xóa authen khi đăng xuất
                    .permitAll()
                 )
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret") //lưu phiên đăng nhập dựa trên cookie.
                        .tokenValiditySeconds(86400) .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedPage("/403")) .build();
    }
}