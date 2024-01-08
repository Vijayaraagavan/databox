package com.vijay.databox.persistence.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.gallery.Video;
import com.vijay.databox.core.model.gallery.VideoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VideoRepositoryAdapter implements VideoRepository {
    @Autowired
    private VideoJpaRepository videoJpaRepository;

    @Override
    public Video save(Video image) {
        return videoJpaRepository.save(image);
    }

    @Override
    public Optional<Video> findById(Long id) {
        return videoJpaRepository.findById(id);
    }

    @Override
    public List<Video> allVideos(User user) {
        return videoJpaRepository.findByUser(user);
    }

    public Optional<Video> findByIdAndVideoId(Long userId, Long id) {
        return videoJpaRepository.findByIdAndVideoId(userId, id);
    }

    public void deleteById(Long id) {
        videoJpaRepository.deleteById(id);
    }

	public Optional<Integer> findMaxIdentifier(Long id, String fileName){
        return videoJpaRepository.findMaxIdentifier(id, fileName);
    }

}
