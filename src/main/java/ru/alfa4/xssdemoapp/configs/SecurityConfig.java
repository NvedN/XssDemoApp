package ru.alfa4.xssdemoapp.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.alfa4.xssdemoapp.services.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserService userDetail;

  @Bean
  public ContentSecurityPolicyHeaderWriter contentSecurityPolicyHeaderWriter(
      @Value("${csp.policy:default-src 'self'}") String cspPolicy) {
    return new ContentSecurityPolicyHeaderWriter(cspPolicy);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/login", "/register", "/static/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            formLogin -> formLogin.loginPage("/login").defaultSuccessUrl("/main", true).permitAll())
        .logout(LogoutConfigurer::permitAll)
        .userDetailsService(userDetail)
        .headers(headers -> headers.addHeaderWriter(contentSecurityPolicyHeaderWriter(null)));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
  }
}
