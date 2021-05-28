package com.excentria_it.wamya.domain;

import org.springframework.core.io.FileSystemResource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {
	private DocumentType type;
	private FileSystemResource resource;
}
