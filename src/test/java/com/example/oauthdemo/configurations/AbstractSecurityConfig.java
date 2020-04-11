package com.example.oauthdemo.configurations;

import com.example.oauthdemo.OAuthDemoApplication;

import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OAuthDemoApplication.class)
public class AbstractSecurityConfig {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
    }

    public String obtainAccessToken(String clientId, String clientSecret, String grantType, String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", grantType);

        if (grantType.equals("password")) {
            params.add("username", username);
            params.add("password", password);
        }

        ResultActions result = mockMvc
                .perform(post("/oauth/token")
                    .params(params)
                    .with(httpBasic(clientId, clientSecret))
                    .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        return new JacksonJsonParser().parseMap(result.andReturn().getResponse().getContentAsString()).get("access_token").toString();

    }

}