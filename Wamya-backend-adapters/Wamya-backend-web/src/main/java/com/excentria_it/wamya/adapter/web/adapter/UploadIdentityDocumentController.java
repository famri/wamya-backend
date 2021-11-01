package com.excentria_it.wamya.adapter.web.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.excentria_it.wamya.application.port.in.UploadIdentityDocumentUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadIdentityDocumentController {
	private final UploadIdentityDocumentUseCase uploadIdentityDocumentUseCase;

	@PostMapping(path = "/identities")
	@ResponseStatus(HttpStatus.CREATED)
	public void uploadProfileImage(@RequestParam MultipartFile document,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) throws IOException {

		uploadIdentityDocumentUseCase.uploadIdentityDocument(new BufferedInputStream(document.getInputStream()),
				document.getOriginalFilename(), principal.getName());

	}
}
