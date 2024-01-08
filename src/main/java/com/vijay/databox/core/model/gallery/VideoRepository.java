package com.vijay.databox.core.model.gallery;

import java.util.List;
import java.util.Optional;

import com.vijay.databox.core.model.User;

public interface VideoRepository {
    Video save(Video video);

    Optional<Video> findById(Long id);

    List<Video> allVideos(User user);

    Optional<Video> findByIdAndVideoId(Long userId, Long id);

    void deleteById(Long id);

	Optional<Integer> findMaxIdentifier(Long id, String fileName);

}
