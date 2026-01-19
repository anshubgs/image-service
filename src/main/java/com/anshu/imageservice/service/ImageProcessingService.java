package com.anshu.imageservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageProcessingService {

    void process(MultipartFile file);
}
