package com.vijay.databox.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
                // Image oldImage = imageRepo.findByName(details.name());
        Long uid = generateId();
        String link = save(file, uid);
        int identifier = fetchName(user, details);
        Image image = new Image(details.name(), link, uid, file.getContentType(), user, new Date(), identifier);
        Image saved = null;
        try {
            saved = imageRepo.save(image);
        } catch (Exception e) {
            System.out.println("caused " + e.getMessage());
        }
        return saved;
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

    public List<Image> getImages(UserJwtDetails userDetails) {
        // List<Image> images = new ArrayList<Image>();
        User user = userRepo.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return imageRepo.allImages(user);
    }

    public List<Image> getImages(User user) {
        return imageRepo.allImages(user);
    }

    public int fetchName(User user, ImageDetails details) {
        Integer identifier = userRepo.findMaxIdentifier(user.getId(), details.name()).orElse(null);
        System.out.println("identi " + identifier);
        if (identifier == null) {
            return 0;
        } else {
            return identifier+1;
        }
    }
}
