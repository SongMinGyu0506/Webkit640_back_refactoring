package com.webkit640.backend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.response.ExceptionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .headers().frameOptions().disable().and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**","/h2-console/**","/auth/**", "/board/download/**"
                        , "/imagePath/**","/main/**"
                        ,"/board/list-review",
                        "/board/list-review/**",
                        "/board/list-image/",
                        "/board/list-image/**","/swagger-ui.html/**").permitAll()
                .anyRequest()
                .authenticated();
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    Map<String,Object> data = new HashMap<String,Object>();
                    data.put("status", HttpServletResponse.SC_FORBIDDEN);
                    data.put("message", authException.getMessage());

                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    objectMapper.writeValue(response.getOutputStream(),data);
                });
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        return http.build();
    }
}
