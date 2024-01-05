package com.vijay.databox.views;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vijay.databox.api.response.ImageResponse;
import com.vijay.databox.core.model.SideTab;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRepository;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.service.GalleryService;
import com.vijay.databox.core.service.UserService;
import com.vijay.databox.views.LoginController.PageContext;
import com.vijay.databox.views.modules.SideNav;

@Controller
public class GalleryView {
    @Autowired
    private GalleryService service;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @GetMapping("/gallery")
    public String doGet(Model model) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // UserJwtDetails details = (UserJwtDetails) auth.getPrincipal();
        UserJwtDetails details = getDetails();
        User user = userService.getUserByUsername(details.getUsername());
        SideTab[] tabs = new SideNav().getNav();
        PageContext ctx = new PageContext(tabs[0].id());
        model.addAttribute("user", user);
        model.addAttribute("sideTabs", tabs);
        model.addAttribute("ctx", ctx);

        // User user = userRepo.findById(id)
        // .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Image> images = service.getImages(user);

        List<ImageResponse> resp = new ArrayList<ImageResponse>();
        for (Image image : images) {
            String name = image.getName();
            if (image.getIdentifier() > 0) {
                String[] names = image.getName().split("\\.");
                name = String.format("%s(%d).%s", names[0], image.getIdentifier(), names[1]);
            }
            String[] splitted = image.getPath().split("/storage");
            String link = "/images/" + image.getImageId() + "_lq.png";
            resp.add(new ImageResponse(name, image.getImageId(), image.getCreatedAt(), splitted[1], link));
        }
        model.addAttribute("userImages", resp);
        return "components/gallery/gallery";
    }

    @GetMapping("/videos")
    public String getVideos(Model model) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // UserJwtDetails details = (UserJwtDetails) auth.getPrincipal();
        UserJwtDetails details = getDetails();
        User user = userService.getUserByUsername(details.getUsername());
        SideTab[] tabs = new SideNav().getNav();
        PageContext ctx = new PageContext(tabs[0].id());
        model.addAttribute("user", user);
        model.addAttribute("sideTabs", tabs);
        model.addAttribute("ctx", ctx);

        // User user = userRepo.findById(id)
        // .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return "components/videos/videos";
    }

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }
}
