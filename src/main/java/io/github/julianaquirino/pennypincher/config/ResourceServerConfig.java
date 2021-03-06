package io.github.julianaquirino.pennypincher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/app-users").permitAll()
                .antMatchers("/api/subcategories/**", "/api/categories/**",
                        "/api/accounts/**", "/api/projects/**", "/api/dailyRecords/**",
                        "/api/records/**", "/api/app-users/**", "/api/limits/**",
                        "/api/goals/**", "/api/moneytransfers/**", "/api/charts/**",
                        "/api/reports/**").authenticated()
                .anyRequest().denyAll();
    }
}
