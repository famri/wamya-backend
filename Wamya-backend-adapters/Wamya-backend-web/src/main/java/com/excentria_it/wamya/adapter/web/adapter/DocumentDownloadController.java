package com.excentria_it.wamya.adapter.web.adapter;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.DownloadDocumentUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.Document;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/documents")
public class DocumentDownloadController {
	private final DownloadDocumentUseCase downloadDocumentUseCase;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<FileSystemResource> getDocumentAsResponseEntity(@PathVariable(name = "id") Long documentId,
			@RequestParam(name = "h") String documentHash) {

		Document document = downloadDocumentUseCase.getDocument(documentId, documentHash);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(document.getType().getMediaType()));

		ResponseEntity<FileSystemResource> responseEntity = new ResponseEntity<>(document.getResource(), headers,
				HttpStatus.OK);

		return responseEntity;
	}
}
