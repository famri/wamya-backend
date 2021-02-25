package com.excentria_it.wamya.adapter.b2b.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrix {

	@JsonProperty(value = "destination_addresses")
	private List<String> destinationAddresses;

	@JsonProperty(value = "origin_addresses")
	private List<String> originAddresses;

	private List<Row> rows;

	private String status;

	public DistanceMatrix() {

	}

	public List<String> getDestinationAddresses() {
		return destinationAddresses;
	}

	public void setDestinationAddresses(List<String> destinationAddresses) {
		this.destinationAddresses = destinationAddresses;
	}

	public List<String> getOriginAddresses() {
		return originAddresses;
	}

	public void setOriginAddresses(List<String> originAddresses) {
		this.originAddresses = originAddresses;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Row {

		public Row() {

		}

		private List<Element> elements;

		public List<Element> getElements() {
			return elements;
		}

		public void setElements(List<Element> elements) {
			this.elements = elements;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Element {
		private Distance distance;

		private Duration duration;

		private String status;

		public Element() {

		}

		public Distance getDistance() {
			return distance;
		}

		public void setDistance(Distance distance) {
			this.distance = distance;
		}

		public Duration getDuration() {
			return duration;
		}

		public void setDuration(Duration duration) {
			this.duration = duration;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Distance {

		private Integer value;

		public Distance() {

		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Duration {

		private Integer value;

		public Duration() {

		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

	}
}
