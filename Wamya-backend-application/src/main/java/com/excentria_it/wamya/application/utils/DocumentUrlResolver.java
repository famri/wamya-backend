package com.excentria_it.wamya.application.utils;

public class DocumentUrlResolver {

	private static final String DOCUMENTS_API_BASE_URL = "/documents";

	private DocumentUrlResolver() {

	}

	public static String resolveUrl(Long documentId, String documentHash) {
		return DOCUMENTS_API_BASE_URL + "/" + documentId + "?hash=" + documentHash;

	}
}
