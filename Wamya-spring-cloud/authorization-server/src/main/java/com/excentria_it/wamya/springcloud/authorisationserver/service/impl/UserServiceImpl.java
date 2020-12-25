package com.excentria_it.wamya.springcloud.authorisationserver.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthRole;
import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthUserAccount;
import com.excentria_it.wamya.springcloud.authorisationserver.dto.UserPrincipal;
import com.excentria_it.wamya.springcloud.authorisationserver.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.RoleRepository;
import com.excentria_it.wamya.springcloud.authorisationserver.repository.UserRepository;
import com.excentria_it.wamya.springcloud.authorisationserver.service.UserService;
import com.excentria_it.wamya.springcloud.authorisationserver.utils.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private UserMapper mapper;

	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByEmail(username);
		if (!userEntity.isPresent()) {
			userEntity = userRepository.findByPhoneNumber(username);
			if (!userEntity.isPresent()) {
				throw new UsernameNotFoundException(username);
			}

		}
		return new UserPrincipal(userEntity.get());
	}

	@Override
	public OAuthUserAccount createUser(OAuthUserAccount user) {
		Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(user.getEmail());
		if (optionalUserEntity.isPresent()) {
			throw new UserAccountAlreadyExistsException(
					String.format("User with email %s already exists.", user.getEmail()));
		}

		optionalUserEntity = userRepository.findByPhoneNumber(user.getPhoneNumber());
		if (optionalUserEntity.isPresent()) {
			throw new UserAccountAlreadyExistsException(
					String.format("User with mobile number %s already exists.", user.getPhoneNumber()));
		}

		// Remove ADMIN and invalid roles
		List<RoleEntity> acceptedRoleEntities = roleRepository.findAll();
		List<String> acceptedRoles = acceptedRoleEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
		Set<OAuthRole> filteredRoles = filterRoles(user, acceptedRoles);
		if (filteredRoles.isEmpty()) {
			throw new RuntimeException(String.format("Invalid user roles."));
		}

		user.setRoles(filteredRoles);

		UserEntity entity = mapper.apiToEntity(user);

		entity.getRoles().forEach(r -> {

			Optional<RoleEntity> roleEntity = roleRepository.findByName(r.getName());
			r.setId(roleEntity.get().getId());
		});

		// Encode password
		entity.setPassword(passwordEncoder.encode(user.getPassword()));

		UserEntity newEntity = userRepository.save(entity);

		return mapper.entityToApi(newEntity);

	}

	private Set<OAuthRole> filterRoles(OAuthUserAccount user, List<String> acceptedRoles) {
		Set<OAuthRole> filteredRoles = Collections.<OAuthRole>emptySet();
		// Disable ADMIN user creation from API
		// Remove invalid RoleType
		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			filteredRoles = user.getRoles().stream().filter(r -> {
				return r != null && !"ROLE_ADMIN".equals(r.getName()) && acceptedRoles.contains(r.getName());
			}).collect(Collectors.toSet());

		}
		return filteredRoles;

	}

	@Override
	public OAuthUserAccount loadUserInfoByUsername(String username) {
		Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);
		if (!userEntityOptional.isPresent()) {
			userEntityOptional = userRepository.findByPhoneNumber(username);
			if (!userEntityOptional.isPresent()) {
				throw new UsernameNotFoundException(username);
			}

		}
		return mapper.entityToApi(userEntityOptional.get());
	}
}
