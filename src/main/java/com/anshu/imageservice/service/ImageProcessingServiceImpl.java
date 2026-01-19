package com.anshu.imageservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageProcessingServiceImpl implements ImageProcessingService{
    @Override
    public void process(MultipartFile file) {

        //TODO: resize or Thumbnail
    }
}
