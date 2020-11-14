/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excentria_it.wamya.springcloud.authorisationserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.RoleRepository;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.UserRepository;

/**
 * Tests for {@link OAuth2AuthorizationServerApplication}
 *
 * @author Josh Cummings
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "eureka.client.enabled=false" })
@ActiveProfiles(value = "localtest")
@AutoConfigureMockMvc
public class OAuth2AuthorizationServerApplicationTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Test
	public void requestTokenWhenUsingPasswordGrantTypeThenOk() throws Exception {

		UserEntity user = new UserEntity();
		user.setFirstname("admin");
		user.setLastname("admin");
		user.setEmail("admin@wamya.com");
		user.setPhoneNumber("+21628093418");
		user.setPassword("{noop}admin");
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);

		Optional<RoleEntity> roleOptional = roleRepo.findByName("ROLE_ADMIN");

		user.setRoles(Collections.singleton(roleOptional.get()));

		userRepo.save(user);

		this.mvc.perform(post("/oauth/token").param("grant_type", "password").param("username", "admin@wamya.com")
				.param("password", "admin").header("Authorization", "Basic d2FteWEtbW9iaWxlLWFwcDpTM0NSM1Q="))
				.andExpect(status().isOk());
	}

	@Test
	public void requestJwkSetWhenUsingDefaultsThenOk() throws Exception {

		this.mvc.perform(get("/.well-known/jwks.json")).andExpect(status().isOk());
	}

}
