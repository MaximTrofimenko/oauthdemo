package com.example.oauthdemo.controllers;

import com.example.oauthdemo.configurations.AbstractSecurityConfig;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VisitorControllerSecuredMvcTest extends AbstractSecurityConfig {

    private final String CLIENT_ID = "geekbrains-client";
    private final String CLIENT_SECRET = "secret";
    private final String GRANT_TYPE = "password";

    private String ADMIN_ACCESS_TOKEN;
    private String USER_ACCESS_TOKEN;

    @Before
    public void setUp() throws Exception {
        ADMIN_ACCESS_TOKEN = obtainAccessToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, "geekadmin", "admin");
        USER_ACCESS_TOKEN = obtainAccessToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE,"geekuser", "user");
    }

    @Test
    public void must_Successfully_Access_Admin_Endpoint_With_Admin_Token() throws Exception {
        mockMvc
            .perform(get("/admin")
            .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN)
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk());
    }

    @Test
    public void must_Successfully_Access_User_Endpoint_With_User_Token() throws Exception {
        mockMvc
            .perform(get("/user")
            .header("Authorization", "Bearer " + USER_ACCESS_TOKEN)
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk());
    }

    @Test
    public void must_Successfully_Access_User_Endpoint_With_Admin_Token() throws Exception {
        mockMvc
            .perform(get("/user")
            .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN)
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk());
    }

    @Test
    public void must_Return_Forbidden_Exception() throws Exception {
        mockMvc
            .perform(get("/admin")
            .header("Authorization", "Bearer " + USER_ACCESS_TOKEN)
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isForbidden());
    }

}