package com.vijay.databox.core.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.gallery.VideoDetails;
import com.vijay.databox.exception.CustomException;

@Service
public class VideoService {
    @Autowired
    private AtomicLong counter;
    @Value("${storage.path}")
    private String location;

    public long saveVideo(MultipartFile file, VideoDetails details, UserJwtDetails userJwtDetails) {
        long uid = generateId(details.videoId());
        String fileType = details.type().split("/")[1];
        String fileName = String.format("%d_%d.%s", uid, details.index(), fileType);
        saveToStorage(fileName, file, uid);
        return uid;
    }

    public long generateId(long id) {
        if (id != -1) {
            return id;
        }
        long random = (long) (Math.random() * 1000);
        return counter.getAndIncrement() + random;
    }

    public void saveToStorage(String name, MultipartFile file, long uid) {
        String dir = location + "/videos/" + uid;
        String filePath = dir + "/" + name;
        createDir(dir);
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new CustomException("Error parsing file");
        }
    }

    private void createDir(String dir) {
        Path path = Paths.get(dir);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + dir);
            } catch (IOException e) {
                System.err.println("Error creating directory: " + e.getMessage());
            }
        }
    }
}
