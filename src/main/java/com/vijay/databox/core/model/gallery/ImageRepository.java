package com.vijay.databox.core.model.gallery;

import java.util.List;
import java.util.Optional;

import com.vijay.databox.core.model.User;

public interface ImageRepository {
    Image save(Image image);

    Optional<Image> findById(Long id);
    List<Image> allImages(User user);

}
