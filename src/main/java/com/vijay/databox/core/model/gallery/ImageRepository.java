package com.vijay.databox.core.model.gallery;

import java.util.Optional;

public interface ImageRepository {
    Image save(Image image);

    Optional<Image> findById(Long id);
}
