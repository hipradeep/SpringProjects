package com.hipradeep.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class SpringProjectsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	private String obtainAccessToken(String username, String password) throws Exception {
		Map<String, String> loginRequest = Map.of(
				"username", username,
				"password", password
		);

		MvcResult result = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andReturn();

		String responseString = result.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseString, Map.class);
		return responseMap.get("token");
	}

	@Test
	void testUserAccessOwnResource() throws Exception {
		String token = obtainAccessToken("user", "password");

		mockMvc.perform(get("/api/users/1")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Access Granted for user resource with ID 1")));
	}

	@Test
	void testUserAccessOtherResourceForbidden() throws Exception {
		String token = obtainAccessToken("user", "password");

		mockMvc.perform(get("/api/users/2")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());
	}

	@Test
	void testAdminAccessOtherResource() throws Exception {
		String token = obtainAccessToken("admin", "password");

		mockMvc.perform(get("/api/users/1")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Access Granted for user resource with ID 1")));
	}

	@Test
	void testAdminAccessOwnResource() throws Exception {
		String token = obtainAccessToken("admin", "password");

		mockMvc.perform(get("/api/users/2")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Access Granted for user resource with ID 2")));
	}
}

