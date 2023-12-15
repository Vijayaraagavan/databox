package com.vijay.databox.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.databox.api.request.UploadImage;
import com.vijay.databox.api.response.ImageResponse;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRepository;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.gallery.ImageDetails;
import com.vijay.databox.core.service.GalleryService;

@RestController
public class GalleryController {
    final String SCOPE = "/api/images";
    @Autowired
    private GalleryService service;
    

    @PostMapping(value = SCOPE, consumes = "multipart/form-data")
    public ImageResponse uploadImage(@RequestPart MultipartFile file, @RequestPart(name = "details") String jsonData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        Object p = auth.getPrincipal();
        UserJwtDetails userDetails = (UserJwtDetails) auth.getPrincipal();
                ObjectMapper ob = new ObjectMapper();
                Image image = null;
        try {
            ImageDetails details = ob.readValue(jsonData, ImageDetails.class);
            System.out.println(details);
            image = service.saveImage(file, details, userDetails);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("invalid image details");
        }
        return new ImageResponse(image.getName(), image.getImageId(), image.getCreatedAt());
        // System.out.println(details);
    }
}
