package com.vijay.databox.core.service;

import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRepository;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.gallery.ImageDetails;
import com.vijay.databox.core.model.gallery.ImageRepository;
import com.vijay.databox.exception.CustomException;

@Service
public class GalleryService {

    @Value("${storage.path}")
    private String location;

    @Autowired
    private AtomicLong counter;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ImageRepository imageRepo;

    public Image saveImage(MultipartFile file, ImageDetails details, UserJwtDetails userDetails) {
        User user = userRepo.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        long uid = generateId();
        String link = save(file, uid);
        Image image = new Image(details.name(), link, uid, file.getContentType(), user, new Date());
        return imageRepo.save(image);
    }

    public String save(MultipartFile file, long uid) {
        // String name = file.getOriginalFilename();
        String imageType = file.getContentType().split("/")[1];
        // String imageType = name.split("\\.(\\w+)$")[1];
        File dest = new File(location + "/images/" + uid + "." + imageType);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new CustomException("Error parsing file");
        }
        return dest.toPath().toString();
    }

    public long generateId() {
        long random = (long) (Math.random() * 1000);
        return counter.getAndIncrement() + random;
    }
}
