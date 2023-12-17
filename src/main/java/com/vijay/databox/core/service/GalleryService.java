package com.vijay.databox.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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
        String newFilePath = location + "/images/" + uid + "." + imageType;
        String lqPath = location + "/images/" + uid + "_lq." + imageType;
        File dest = new File(newFilePath);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new CustomException("Error parsing file");
        }
        createLowQuality(dest, lqPath);
        return dest.toPath().toString();
    }

    public Image deleteImage(UserJwtDetails userDetails, long imageId) {
        User user = userRepo.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Image img = imageRepo.findByIdAndImageId(user.getId(), imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
        imageRepo.deleteById(img.getId());
        return img;
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
            return identifier + 1;
        }
    }

    public void createLowQuality(File file, String path) {
        int width = 720;
        int height = 720;
        try {
            BufferedImage ogImage = ImageIO.read(file);
            BufferedImage resizedImage = resize(ogImage, width, height);
            saveLqImage(resizedImage, path);
        } catch (Exception e) {
            System.out.println("error in img process: " + e.getMessage());
        }

    }

    public BufferedImage resize(BufferedImage ogImage, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(ogImage, 0, 0, width, height, null);
        g.dispose();
        return img;
    }

    public void saveLqImage(BufferedImage resizedImage, String path) {
        try {
            ImageIO.write(resizedImage, "PNG", new File(path));

        } catch (Exception e) {
            System.out.println("error in img saving: " + e.getMessage());
        }
    }
}
