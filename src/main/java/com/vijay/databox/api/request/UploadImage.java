package com.vijay.databox.api.request;

import org.springframework.web.multipart.MultipartFile;

public record UploadImage(MultipartFile file) {
    
}
