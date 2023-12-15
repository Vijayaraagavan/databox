package com.vijay.databox.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        UserJwtDetails userDetails = getDetails();
        ObjectMapper ob = new ObjectMapper();
        Image image = null;
        try {
            ImageDetails details = ob.readValue(jsonData, ImageDetails.class);
            System.out.println(details);
            image = service.saveImage(file, details, userDetails);
        } catch (JsonProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("invalid image details");
        }
        return new ImageResponse(image.getName(), image.getImageId(), image.getCreatedAt());
        // System.out.println(details);
    }

    @GetMapping(value = "api/images/{id}")
    public List<ImageResponse> doIndex(@PathVariable(name = "id") long id) {
        UserJwtDetails details = getDetails();
        List<Image> images = service.getImages(details);
        System.out.println(id);
        List<ImageResponse> resp = new ArrayList<ImageResponse>();
        for (Image image : images) {
            String name = String.format("%s(%d)", image.getName(), image.getIdentifier());
            resp.add(new ImageResponse(name, image.getImageId(), image.getCreatedAt()));
        }
        return resp;
    }

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }
}
