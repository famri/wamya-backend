package com.excentria_it.wamya.springcloud.authorisationserver.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.User;
import com.excentria_it.wamya.springcloud.authorisationserver.dto.UserPrincipal;
import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.RoleRepository;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.UserRepository;
import com.excentria_it.wamya.springcloud.authorisationserver.service.UserService;
import com.excentria_it.wamya.springcloud.authorisationserver.utils.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private UserMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByEmail(username);
		if (userEntity.isEmpty()) {
			userEntity = userRepository.findByPhoneNumber(username);
			if (userEntity.isEmpty()) {
				throw new UsernameNotFoundException(username);
			}

		}
		return new UserPrincipal(userEntity.get());
	}

	@Override
	public User createUser(User user) {
		Optional<UserEntity> userEntity = userRepository.findByEmail(user.getEmail());
		if (userEntity.isPresent()) {
			throw new RuntimeException(String.format("User with email %s already exists.", user.getEmail()));
		}

		userEntity = userRepository.findByPhoneNumber(user.getPhoneNumber());
		if (userEntity.isPresent()) {
			throw new RuntimeException(
					String.format("User with mobile number %s already exists.", user.getPhoneNumber()));
		}

		UserEntity entity = mapper.apiToEntity(user);

		entity.getRoles().forEach(r -> {

			Optional<RoleEntity> roleEntity = roleRepository.findByName(r.getName());
			r.setId(roleEntity.get().getId());
			r.setPrivileges(roleEntity.get().getPrivileges());
		});

		UserEntity newEntity = userRepository.save(entity);

		return mapper.entityToApi(newEntity);

	}
}
