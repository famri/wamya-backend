package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DocumentTypeTests {
	@Test
	void testFromImageJpeg() {
		DocumentType image = DocumentType.from("image/jpeg");
		assertEquals(image, DocumentType.IMAGE_JPEG);
	}

	@Test
	void testFromApplicationPdf() {
		DocumentType pdf = DocumentType.from("application/pdf");
		assertEquals(pdf, DocumentType.APPLICATION_PDF);
	}
}
