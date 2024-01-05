package com.vijay.databox.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.vijay.databox.core.modules.Range;
import com.vijay.databox.core.modules.RangeResource;
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

    @GetMapping(value = "/api/videos/{id}")
    public ResponseEntity<Resource> getVideo(HttpServletRequest request) {
        // UserJwtDetails userDetails = getDetails();
        Resource videoResource = new FileSystemResource(service.getVideo(0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        String rangeHeader = request.getHeader("range");
        System.out.println("range: " + rangeHeader);
        return ResponseEntity.ok().headers(headers).body(videoResource);
    }

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }
}
