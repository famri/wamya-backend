package com.excentria_it.wamya.adapter.file.storage;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { FileStorageConfiguration.class })
@ActiveProfiles("file-storage-local")
public class FileStorageTestConfiguration {

}
