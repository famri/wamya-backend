package com.excentria_it.wamya.springcloud.authorisationserver.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account", indexes = { @Index(name = "email_index", columnList = "email", unique = true),
		@Index(name = "phoneNumber_index", columnList = "phoneNumber", unique = true) })
@SequenceGenerator(name = UserEntity.USER_SEQ, initialValue = 1, allocationSize = 5)
@NoArgsConstructor
@Data
public class UserEntity {

	public static final String USER_SEQ = "user_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQ)
	private Long oauthId;

	@Version
	private Integer version;

	@Column(nullable = false)
	private String firstname;

	@Column(nullable = false)
	private String lastname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean isAccountNonExpired;

	@Column(nullable = false)
	private boolean isAccountNonLocked;

	@Column(nullable = false)
	private boolean isCredentialsNonExpired;

	@Column(nullable = false)
	private boolean isEnabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", uniqueConstraints = {
			@UniqueConstraint(name = "unique_userid_role_id", columnNames = { "user_id",
					"role_id" }) }, joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "oauthId"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<RoleEntity> roles;

}
