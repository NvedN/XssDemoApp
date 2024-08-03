Thymeleaf автоматически экранирует данные, вставленные в HTML-шаблоны по умолчанию. Этот процесс гарантирует, что любые
данные, включенные в HTML-шаблоны, обрабатываются таким образом, чтобы предотвратить выполнение вредоносного кода
JavaScript. Например:

```html
<p th:text="${user.name}"></p>
```

В этом случае, если значение ${user.name} содержит вредоносный код, он будет экранирован и не выполнится как скрипт.


---

Использование Content Security Policy (CSP)

Content Security Policy - это механизм, предназначенный для предотвращения несанкционированного выполнения кода. В
Spring
Boot можно настроить CSP с помощью HTTP-заголовков. Например:

```java

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
```

Spring Security предоставляет дополнительные механизмы защиты от XSS, такие как фильтрация и экранирование данных. Для
защиты от XSS атак также важно настроить Spring Security для использования правильных HTTP заголовков и политик
безопасности.