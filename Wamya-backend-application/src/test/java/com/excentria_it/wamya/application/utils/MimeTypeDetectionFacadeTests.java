package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class MimeTypeDetectionFacadeTests {
	@Test
	void testDetectContentTypeOfJpegImage() throws IOException {
		InputStream imageInputStream = MimeTypeDetectionFacadeTests.class.getResourceAsStream("/Image.jpg");
		try {

			String mimeType = MimeTypeDetectionFacade.detectContentType(imageInputStream);
			assertEquals("image/jpeg", mimeType);
		} finally {
			imageInputStream.close();
		}
	}
	
	@Test
	void testDetectContentTypeOfPdf() throws IOException {
		InputStream imageInputStream = MimeTypeDetectionFacadeTests.class.getResourceAsStream("/Pdf.pdf");
		try {

			String mimeType = MimeTypeDetectionFacade.detectContentType(imageInputStream);
			assertEquals("application/pdf", mimeType);
		} finally {
			imageInputStream.close();
		}
	}
}
