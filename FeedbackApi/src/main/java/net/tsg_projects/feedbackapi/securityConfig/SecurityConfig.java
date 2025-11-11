package net.tsg_projects.feedbackapi.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {

        http.
                cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())

                // Permit all incoming requests
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())

                //Disable basic auth and form login
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());
        return http.build();
    }
}
