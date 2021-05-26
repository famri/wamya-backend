package com.excentria_it.wamya.adapter.file.storage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = { "file-storage-docker" })
public class FileStorageModuleDockerTest {

	@Test
	void testAllBeansLoaded() {

	}

}
