package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;
import com.excentria_it.wamya.domain.TransporterVehicles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface LoadVehiculesUseCase {

	TransporterVehicles loadTransporterVehicules(LoadVehiculesCommand command, String locale);

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	class LoadVehiculesCommand {
		@NotNull
		private String transporterUsername;
	

		@Sort(fields = { "creation-date-time", "date-time" })
		private SortCriterion sortingCriterion;

	}

}
