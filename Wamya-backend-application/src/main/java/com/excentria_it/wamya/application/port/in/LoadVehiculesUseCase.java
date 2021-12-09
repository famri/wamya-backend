package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.TransporterVehicules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadVehiculesUseCase {

	TransporterVehicules loadTransporterVehicules(LoadVehiculesCommand command, String locale);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadVehiculesCommand {
		@NotNull
		private String transporterUsername;
		@NotNull
		private Integer pageNumber;
		@NotNull
		private Integer pageSize;

		@Sort(fields = { "creation-date-time", "date-time" })
		private SortCriterion sortingCriterion;

	}

}
