package com.vijay.databox.persistence.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.User;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    Image save(Image image);

    Optional<Image> findById(long id);

    List<Image> findByUser(User user);

    @Query(value = "select * from gallery_images where user_id = ?1 and image_id = ?2", nativeQuery = true)
    Optional<Image> findByIdAndImageId(long userId, long id);

    void deleteById(Long id);
}
