package com.excentria_it.wamya.common;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodCriterion {

	private String value;

	private ZonedDateTime lowerEdge;

	private ZonedDateTime higherEdge;

	public enum PeriodValue {
		LM3, LM1, W1, M1;

		public ZonedDateTime[] calculateLowerAndHigherEdges(ZonedDateTime today) {

			ZonedDateTime[] lowerAndHigherEdges = new ZonedDateTime[2];

			switch (this) {

			case LM3:
				lowerAndHigherEdges[0] = today.minusMonths(3);
				lowerAndHigherEdges[1] = today;

				break;
			case LM1:

				lowerAndHigherEdges[0] = today.minusMonths(1);
				lowerAndHigherEdges[1] = today;

				break;

			case W1:

				lowerAndHigherEdges[0] = today;
				lowerAndHigherEdges[1] = today.plusWeeks(1);

				break;
			case M1:
				lowerAndHigherEdges[0] = today;
				lowerAndHigherEdges[1] = today.plusMonths(1);
				break;

			default:
				break;
			}

			return lowerAndHigherEdges;
		}
	}
}
