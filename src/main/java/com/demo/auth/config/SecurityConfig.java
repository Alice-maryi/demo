package com.demo.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.demo.auth.filter.TokenAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${system.security.ignoringUrl}")
    private String ignoringUrl;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 配置自定义认证数据源，替换过滤器
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AccessDeniedHandler accessDeniedHandler = (request, response, ex) -> {
            ex.printStackTrace();
            new ObjectMapper().writeValue(response.getWriter(), new JSONObject() {{
                put("code", 401);
                put("message", "access denied");
            }});
        };
        AuthenticationEntryPoint authenticationEntryPoint = (request, response, ex) -> {
            new ObjectMapper().writeValue(response.getWriter(), new JSONObject() {{
                put("code", 401);
                put("message", "not auth");
            }});
        };
        return http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests().antMatchers(StringUtils.trimArrayElements(ignoringUrl.split(","))).permitAll().anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new TokenAuthenticationFilter(authenticationManager, userDetailsService))
                .csrf().disable()
                .build();
    }






}
