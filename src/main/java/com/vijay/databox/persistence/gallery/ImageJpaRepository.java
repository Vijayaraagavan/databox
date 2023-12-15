package com.vijay.databox.persistence.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.User;


public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    Image save(Image image);

    Optional<Image> findById(long id);

    List<Image> findByUser(User user);

}
