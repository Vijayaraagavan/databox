package com.vijay.databox.persistence.gallery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.databox.core.model.gallery.Image;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    Image save(Image image);
    Optional<Image> findById(long id);
} 
