package com.example.ProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static  org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProfileServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MockRestServiceServer server;

	@Test
	public void getProfilesShouldReturnEmptyArray() throws Exception{
		this.mockMvc.perform(get("/PS/profiles"))
            .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	/*@Test
	public void putProfileShouldSucceed() throws Exception {
	Profile profile = new
			Profile(1, "Nom", "test@example.com");
	ObjectMapper objectMapper = new ObjectMapper();
	String profile_json = objectMapper.writeValueAsString(profile);

    this.mockMvc.perform(put("/PS/profiles")
			.content(profile_json)
    .contentType(MediaType.APPLICATION_JSON))

				.andDo(print())
			.andExpect(status().isOk());
	}*/

	@Test
	public void putNameShouldChangeName() throws Exception{
		server.expect(once(), requestTo("http://localhost:8081/AS/users")).andExpect(method(HttpMethod.PUT)).andRespond(withSuccess("1", MediaType.APPLICATION_JSON));

		server.expect(manyTimes(), requestTo("http://localhost:8081/AS/token")).andExpect(method(HttpMethod.GET)).andRespond(withSuccess("1", MediaType.APPLICATION_JSON));

		Profile profile = new
				Profile(1, "Nom", "test@example.com");
		/*String new_name = "Nom2";
		Profile profile2 = new Profile(1, new_name, "test@example.com");*/
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(profile);

		this.mockMvc.perform(put("/PS/profiles")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON))

				.andDo(print())
				.andExpect(status().isOk());
		this.mockMvc.perform(delete("/PS/profiles/1").header("X-Token", "1e"))
				.andDo(print())
				.andExpect(status().isOk());
		this.mockMvc.perform(get("/PS/profiles/1/name"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}


}
