package com.example.oauthdemo.configurations;

import com.example.oauthdemo.services.db.VisitorService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class OAuthServerConfiguration {

    private final DataSource dataSource;
    private final DataSourceConfiguration dataSourceConfiguration;
    private final WebSecurityConfiguration webSecurityConfiguration;

    @Configuration
    @EnableAuthorizationServer
    @RequiredArgsConstructor
    protected class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        private final AuthenticationManager authenticationManager;
        private final VisitorService visitorService;

        public void configure(final AuthorizationServerEndpointsConfigurer endpointsConfigurer) {
            endpointsConfigurer
                .accessTokenConverter(webSecurityConfiguration.jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(visitorService)
                .reuseRefreshTokens(false)
                .tokenStore(dataSourceConfiguration.getTokenStore());
        }

        @Override
        public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource);
        }

    }

    @Configuration
    @EnableResourceServer
    @RequiredArgsConstructor
    protected class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        private static final String RESOURCE_ID = "oauth-demo-api";

        @Override
        public void configure(final ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(dataSourceConfiguration.getTokenStore());
            resources.resourceId(RESOURCE_ID);
        }

        public void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/webjars/**" , "/swagger-resources/**", "/v2/**")
                .permitAll().and().formLogin();
        }

    }

}