package com.vijay.databox.persistence.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.gallery.Video;

public interface VideoJpaRepository extends JpaRepository<Video, Long> {
    Video save(Video video);

    Optional<Video> findById(Long id);

    List<Video> findByUser(User user);

    @Query(value = "select * from videos where user_id = ?1 and video_id = ?2", nativeQuery = true)
    Optional<Video> findByIdAndVideoId(Long userId, Long id);

    void deleteById(Long id);

    @Query(value = "select max(identifier) from videos where user_id = ?1 and name = ?2", nativeQuery = true)
    Optional<Integer> findMaxIdentifier(Long id, String fileName);
}
