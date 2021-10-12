package com.example.ProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static  org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProfileServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getProfilesShouldReturnEmptyArray() throws Exception{
		this.mockMvc.perform(get("/PS/profiles"))
            .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
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
	}

}
