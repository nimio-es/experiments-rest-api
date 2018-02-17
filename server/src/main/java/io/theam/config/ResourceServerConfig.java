package io.theam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("res_customer_api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous().disable()
                .requestMatchers()
                .antMatchers("/users/**")
                .antMatchers("/customers/**")
                .antMatchers("/images/**")
                .antMatchers("/products/**")
                .and()
                .authorizeRequests()
                .antMatchers("/users/**").access("hasAnyRole('ADMIN', 'OWNER')")
                .antMatchers("/customers/**").access("hasRole('ADMIN')")
                .antMatchers("/images/**").access("hasRole('ADMIN')")
                .antMatchers("/products/**").access("hasRole('ADMIN')")
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
