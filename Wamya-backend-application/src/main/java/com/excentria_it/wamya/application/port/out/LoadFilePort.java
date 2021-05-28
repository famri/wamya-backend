package com.excentria_it.wamya.application.port.out;

import java.io.IOException;

import org.springframework.core.io.FileSystemResource;

public interface LoadFilePort {

	FileSystemResource loadFile(String location) throws IOException;

}
