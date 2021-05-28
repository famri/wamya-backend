package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;

import com.excentria_it.wamya.application.port.out.LoadDocumentInfoPort;
import com.excentria_it.wamya.application.port.out.LoadFilePort;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.domain.Document;
import com.excentria_it.wamya.domain.DocumentInfo;
import com.excentria_it.wamya.domain.DocumentType;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTests {

	@Mock
	private LoadDocumentInfoPort loadDocumentInfoPort;
	@Mock
	private LoadFilePort loadFilePort;

	@InjectMocks
	private DocumentService documentService;

	@Test
	void testGetDocument() throws IOException, URISyntaxException {

		// given
		URL res = DocumentServiceTests.class.getResource("/Image.jpg");
		File file = Paths.get(res.toURI()).toFile();
		String location = file.getAbsolutePath();

		DocumentInfo documentInfo = new DocumentInfo(DocumentType.IMAGE_JPEG, location);

		given(loadDocumentInfoPort.getDocumentInfo(any(Long.class))).willReturn(documentInfo);

		FileSystemResource resource = new FileSystemResource(Paths.get(location));

		given(loadFilePort.loadFile(location)).willReturn(resource);

		// when
		Document document = documentService.getDocument(1L, "someHash");

		// then
		assertEquals(documentInfo.getType(), document.getType());
		assertEquals(resource, document.getResource());

	}

	@Test
	void givenInexistentDocument_whenGetDocument_thenReturnNull() throws IOException, URISyntaxException {

		// given

		given(loadDocumentInfoPort.getDocumentInfo(any(Long.class))).willReturn(null);

		// when
		Document document = documentService.getDocument(1L, "someHash");

		// then
		assertNull(document);

	}

	@Test
	void givenIOException_whenGetDocument_thenThrowDocumentAccessException() throws URISyntaxException, IOException {
		// given
		URL res = DocumentServiceTests.class.getResource("/Image.jpg");
		File file = Paths.get(res.toURI()).toFile();
		String location = file.getAbsolutePath();

		DocumentInfo documentInfo = new DocumentInfo(DocumentType.IMAGE_JPEG, location);

		given(loadDocumentInfoPort.getDocumentInfo(any(Long.class))).willReturn(documentInfo);

		doThrow(IOException.class).when(loadFilePort).loadFile(location);

		// when // then
		assertThrows(DocumentAccessException.class, () -> documentService.getDocument(1L, "someHash"));

	}
}
