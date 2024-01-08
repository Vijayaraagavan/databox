package com.vijay.databox.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.databox.api.response.VideoResponse;
import com.vijay.databox.api.response.VideoStreamResponse;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.gallery.ImageDetails;
import com.vijay.databox.core.model.gallery.Video;
import com.vijay.databox.core.model.gallery.VideoDetails;
import com.vijay.databox.core.modules.Range;
import com.vijay.databox.core.modules.RangeResource;
import com.vijay.databox.core.service.VideoService;
import org.springframework.web.bind.annotation.RequestParam;

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
            Video video = service.saveVideo(file, details, userDetails);
            videoId = video.getVideoId();
        } catch (JsonProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("invalid image details");
        }
        return new VideoResponse(videoId);
    }

    @GetMapping(value = "/api/videos/{id}")
    public ResponseEntity<Resource> getVideo(HttpServletRequest request) {
        // UserJwtDetails userDetails = getDetails();
        Resource videoResource = new FileSystemResource("/Users/vijay/Downloads/Jawan.mkv");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        String rangeHeader = request.getHeader("range");
        System.out.println("range: " + rangeHeader);
        return ResponseEntity.ok().headers(headers).body(videoResource);
    }

    @GetMapping("/api/video/{id}")
    public ResponseEntity<Resource> streamVideo(HttpServletRequest request, @PathVariable Long id) {
        Video video = service.getVideo(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        combineFiles(video);
        Resource videoResource = new FileSystemResource(video.getPath() + "/main");
        return ResponseEntity.ok().headers(headers).body(videoResource);

    }

    void combineFiles(Video video) {
        String[] chunks = video.getChunks();
        String path = video.getPath();
        // System.out.println("chunks: " + Arrays.toString(chunks));
        Path dest = Path.of(path + "/main");
        StandardOpenOption option = StandardOpenOption.CREATE;
        for (int i = 0; i < chunks.length; i++) {
            if (i != 0) {
                option = StandardOpenOption.APPEND;
            }
            try {
                Files.write(dest, Files.readAllBytes(Path.of(path + "/" + chunks[i])), option);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                // TODO: handle exception
            }
        }

    }

    @GetMapping("/api/videostream/{id}")
    public ResponseEntity<Resource> streamVideos(HttpServletRequest request, @PathVariable Long id) {
        String rangeString = request.getHeader("range");
        Range range = Range.parseRange(rangeString);
        // System.out.println(range.toString());
        HttpHeaders headers = new HttpHeaders();
        VideoStreamResponse resp = service.getChunk(range, id);
        headers.setContentType(MediaType.valueOf("video/mkv"));
        long contentLength = range.getEnd() - range.getStart()+1;
        headers.set("Content-Range", range.getContentRange(contentLength));
        headers.set("Content-Length", String.valueOf(range.getEnd() - range.getStart()));
        headers.set("Accept-Ranges", "bytes");
        // System.out.println("range resp: " + range.getContentRange(contentLength));
        Resource videoResource = new FileSystemResource(resp.path());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(videoResource);
        // FileInputStream file = null;
        // try {
        //    file = new FileInputStream(resp.path());
        // } catch (Exception e) {
        //     System.err.println(e.getMessage());
        // }
        // return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(new InputStreamResource(file));

    }

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }

    
}
