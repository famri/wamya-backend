package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentInfo {
	private DocumentType type;
	private String location;
}
