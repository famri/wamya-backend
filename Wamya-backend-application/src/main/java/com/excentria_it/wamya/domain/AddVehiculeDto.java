package com.excentria_it.wamya.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddVehiculeDto {

	
	private Long id;
	
	private String constructorName;

	private String modelName;

	private String engineType;

	@DateTimeFormat(iso = ISO.DATE)
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate circulationDate;

	private String registration;

	private String photoUrl;

}
