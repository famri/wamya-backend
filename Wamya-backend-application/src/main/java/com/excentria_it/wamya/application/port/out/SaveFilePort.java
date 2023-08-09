package com.excentria_it.wamya.application.port.out;

import java.io.IOException;
import java.io.InputStream;

public interface SaveFilePort {

	String saveFile(InputStream inputStream, String parentFolderName, String fileName) throws IOException;

}
