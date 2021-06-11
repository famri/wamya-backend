package com.excentria_it.wamya.application.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class DocumentUrlResolver {

	@Value("${app.server.base.url}")
	private String serverBaseUrl;

	private String documentsApiBaseUrl = "/documents";

	public String resolveUrl(Long documentId, String documentHash) {
		return serverBaseUrl + documentsApiBaseUrl + "/" + documentId + "?h=" + documentHash;

	}
}
