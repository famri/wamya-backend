package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JourneyProposal {
	
	private Long id;

	private Integer price;
	
	private String transporterId;

	private String transporterName;
	
	private String transporterPhotoUrl;
	
	private Double transporterRating;
	
	private String vehiculePhotoUrl;
	
	private String vehiculeModel;
	
	private String vehiculeConstructor;
	
	
}
