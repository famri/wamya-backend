package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.application.port.in.UploadVehicleImageUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadVehicleImageController {
    private final UploadVehicleImageUseCase uploadVehicleImageUseCase;

    @PostMapping(path = "/{id}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadProfileImage(@RequestParam MultipartFile image, @PathVariable(name = "id") Long vehicleId,
                                   final @AuthenticationPrincipal JwtAuthenticationToken principal) throws IOException {

        uploadVehicleImageUseCase.uploadVehicleImage(new BufferedInputStream(image.getInputStream()),
                image.getOriginalFilename(), vehicleId, principal.getName());

    }
}