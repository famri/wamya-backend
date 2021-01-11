package com.excentria_it.wamya.application.port.in;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AddVehiculeUseCase {

	AddVehiculeDto addVehicule(AddVehiculeCommand command, String transporterUsername, String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class AddVehiculeCommand {

		private Long constructorId;

		private Long modelId;

		private String constructorName;

		private String modelName;

		@NotNull
		private Long engineTypeId;

		@NotNull
		@DateTimeFormat(iso = ISO.DATE)
		@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
		private LocalDate circulationDate;

		@NotNull
		@NotEmpty
		private String registration;

	}

}
