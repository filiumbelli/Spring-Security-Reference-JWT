package com.sofisticat.security.security;


import com.sofisticat.security.auth.ApplicationUserService;
import com.sofisticat.security.jwt.JwtConfig;
import com.sofisticat.security.jwt.JwtSecretKey;
import com.sofisticat.security.jwt.JwtTokenVerifier;
import com.sofisticat.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

import static com.sofisticat.security.security.permission.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {


    private final PasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(String.valueOf(AppSecurityConfig.class));
    private final ApplicationUserService service;
    private final JwtSecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;

    public AppSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService service, JwtSecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtSecretKey.getSecretKey()))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtSecretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
//                .antMatchers("/api/v1/students/**").hasRole(STUDENT.name())
//                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers("/management/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                .anyRequest()
                .authenticated();


//                .and()
//                .httpBasic();
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/courses", true)
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .and()
//
//                .rememberMe()
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .key("somethingsecured")
//                .rememberMeParameter("remember-me")
//                .and()
//
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                .logoutUrl("/logout")
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("remember-me", "JSESSIONID")
//                .logoutSuccessUrl("/login");
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails studentUser = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("1234"))
//                .authorities(STUDENT.getGrantedAuthorities())
//                .roles(ADMIN.name())
//                .build();
//
//        UserDetails linda = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("1234"))
//                .roles(ApplicationUserRole.ADMIN.name())
//                .authorities(ADMIN.getGrantedAuthorities())
//
//                .build();
//        UserDetails tommy = User.builder()
//                .username("tommy")
//                .password(passwordEncoder.encode("1234"))
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
//                .roles(ADMINTRAINEE.name())
//                .build();
//
//
//        return new InMemoryUserDetailsManager(
//                studentUser, linda, tommy
//        );
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(service);
        return provider;
    }

}
