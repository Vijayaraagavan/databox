package com.vijay.databox.persistence.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.gallery.Image;
import com.vijay.databox.core.model.gallery.ImageRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryAdapter implements ImageRepository {
    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Override
    public Image save(Image image) {
        return imageJpaRepository.save(image);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id);
    }

    @Override
    public List<Image> allImages(User user) {
        return imageJpaRepository.findByUser(user);
    }

    public Optional<Image> findByIdAndImageId(Long userId, Long id) {
        return imageJpaRepository.findByIdAndImageId(userId, id);
    }

    public void deleteById(Long id) {
        imageJpaRepository.deleteById(id);
    }
}
