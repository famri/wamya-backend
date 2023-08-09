package com.excentria_it.wamya.adapter.b2b.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodeResponse {

	private String status;

	private List<AddressComponenetsObject> results;

	public GeoCodeResponse() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AddressComponenetsObject> getResults() {
		return results;
	}

	public void setResults(List<AddressComponenetsObject> results) {
		this.results = results;
	}

	public static class AddressComponenetsObject {
		@JsonProperty(value = "address_components")
		private List<AddressComponenetObject> addressComponents;

		public AddressComponenetsObject() {

		}

		public List<AddressComponenetObject> getAddressComponents() {
			return addressComponents;
		}

		public void setAddressComponents(List<AddressComponenetObject> addressComponents) {
			this.addressComponents = addressComponents;
		}

	}

	public static class AddressComponenetObject {
		@JsonProperty(value = "long_name")
		private String longName;

		private List<String> types;

		public AddressComponenetObject() {

		}

		public String getLongName() {
			return longName;
		}

		public void setLongName(String longName) {
			this.longName = longName;
		}

		public List<String> getTypes() {
			return types;
		}

		public void setTypes(List<String> types) {
			this.types = types;
		}

	}
}
