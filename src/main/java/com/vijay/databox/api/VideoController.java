package com.vijay.databox.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.databox.api.response.VideoResponse;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.gallery.ImageDetails;
import com.vijay.databox.core.model.gallery.VideoDetails;
import com.vijay.databox.core.service.VideoService;

@RestController
public class VideoController {
    @Autowired
    VideoService service;

    @PostMapping(value = "/api/videos", consumes = "multipart/form-data")
    public VideoResponse doPost(@RequestPart MultipartFile file, @RequestPart(name = "details") String jsonData) {
        UserJwtDetails userDetails = getDetails();
        ObjectMapper ob = new ObjectMapper();
        // Image image = null;
        long videoId;
        try {
            VideoDetails details = ob.readValue(jsonData, VideoDetails.class);
            System.out.println(details);
            videoId = service.saveVideo(file, details, userDetails);
        } catch (JsonProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("invalid image details");
        }
        return new VideoResponse(videoId);
    }

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }
}
