package com.vijay.databox.persistence.gallery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vijay.databox.core.model.gallery.VideoChunk;

public interface VideoChunkJpaRepository extends JpaRepository<VideoChunk, Long> {
    VideoChunk save(VideoChunk VideoChunk);

    // Optional<VideoChunk> findById(Long id);

    // List<VideoChunk> allVideoChunks(User user);

    // Optional<VideoChunk> findByIdAndVideoChunkId(Long userId, Long id);

    // void deleteById(Long id);

    // Optional<Integer> findMaxIdentifier(Long id, String fileName);

    @Query(value = "select * from video_chunks where chunk_start <= ?1 and chunk_end > ?1 and video_id = ?2 limit 1", nativeQuery = true)
    Optional<VideoChunk> findByRange(long start, long videoId);

    @Query(value = "select * from video_chunks where chunk_start >= ?1 and video_id = ?2 limit 1", nativeQuery = true)
    Optional<VideoChunk> findFirstChunk(long start, long videoId);
}
