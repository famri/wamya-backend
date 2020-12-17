package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDto {

	private Long id;

	private String firstname;

	private String photoUrl;
}
