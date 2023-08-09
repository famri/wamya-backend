package com.excentria_it.wamya.application.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

public class MimeTypeDetectionFacade {

	private MimeTypeDetectionFacade() {

	}

	public static String detectContentType(InputStream input) throws IOException {

		Detector detector = new DefaultDetector();
		Metadata metadata = new Metadata();
		MediaType mediaType = detector.detect(input, metadata);

		return mediaType.toString();
	}
}
