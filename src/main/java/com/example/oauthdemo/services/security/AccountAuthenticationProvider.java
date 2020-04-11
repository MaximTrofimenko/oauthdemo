package com.example.oauthdemo.services.security;

import com.example.oauthdemo.configurations.CryptoEncodingConfiguration;
import com.example.oauthdemo.services.db.VisitorService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final CryptoEncodingConfiguration cryptoEncodingConfiguration;
    private final VisitorService visitorService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException("Bad credentials!");
        }

        if (!cryptoEncodingConfiguration.passwordEncoder().matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials!");
        }

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        UserDetails userDetails = visitorService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Bad credentials!");
        } else {
            return userDetails;
        }

    }

}