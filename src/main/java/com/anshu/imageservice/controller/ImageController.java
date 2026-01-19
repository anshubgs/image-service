package com.anshu.imageservice.controller;

import com.anshu.imageservice.dto.ImageUploadResponse;
import com.anshu.imageservice.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imgService;
    public ImageController(ImageService imgService){
        this.imgService = imgService;

    }


//    @PostMapping
//    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestHeader("X-DEVICE-UUID") UUID deviceUuid,
//                                                           @RequestHeader("X-DEVICE-SECRET") String deviceSecret,
//                                                           @RequestParam("file") @NotNull MultipartFile file){
//
//        ImageUploadResponse response = imgService.uploadImage(deviceUuid,deviceSecret,file);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping(
            consumes = MediaType.IMAGE_JPEG_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestHeader("X-DEVICE-UUID") UUID deviceUuid,
            @RequestHeader("X-DEVICE-SECRET") String deviceSecret,
            HttpServletRequest request
    ) throws IOException {

        log.info("[UPLOAD] Image request started from device={}", deviceUuid);

        // 🔥 SAFE RAW READ (NO CONVERTER, NO BUFFERING)
        byte[] imageBytes = request.getInputStream().readAllBytes();

        log.info("[UPLOAD] Image bytes received, size={} bytes", imageBytes.length);

        ImageUploadResponse response =
                imgService.uploadImage(deviceUuid, deviceSecret, imageBytes);

        log.info("[UPLOAD] Image accepted. metadataUuid={}",
                response.getImageMetadataUuid());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
