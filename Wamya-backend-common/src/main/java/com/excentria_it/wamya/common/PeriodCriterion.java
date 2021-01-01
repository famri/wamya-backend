package com.excentria_it.wamya.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodCriterion {

	private String value;

	private LocalDateTime lowerEdge;

	private LocalDateTime higherEdge;

	public enum PeriodValue {
		Y1, M6, M3, M1, W2, W1;

		public LocalDateTime calculateLowerEdge(LocalDateTime higherEdge) {

			LocalDateTime lowerEdge = null;
			switch (this) {
			case Y1:
				lowerEdge = higherEdge.minusYears(1);
				break;
			case M6:
				lowerEdge = higherEdge.minusMonths(6);
				break;
			case M3:
				lowerEdge = higherEdge.minusMonths(3);
				break;
			case M1:
				lowerEdge = higherEdge.minusMonths(1);
				break;
			case W2:
				lowerEdge = higherEdge.minusWeeks(2);
				break;
			case W1:
				lowerEdge = higherEdge.minusWeeks(1);
				break;
			default:
				break;
			}
			return lowerEdge;
		}
	}
}
