package uk.ac.bradford.projecttwo.webinterface.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for Spring Security configuration.
 * This class verifies access control rules for different user roles.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that an unauthenticated user cannot access the admin dashboard.
     * The expected response should be 401 Unauthorized.
     */
    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests that an admin user can successfully access the admin dashboard.
     * The expected response should be 200 OK.
     */
    @Test
    @WithMockUser(username = "mustafakamran46@hotmail.com", authorities = "ADMIN")
    public void testAdminAccess() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }

    /**
     * Tests that a regular user (USER role) is denied access to the admin dashboard.
     * The expected response should be 403 Forbidden.
     */
    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testUserAccessDenied() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }
}
