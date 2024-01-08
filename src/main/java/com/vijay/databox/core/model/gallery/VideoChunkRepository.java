package com.vijay.databox.core.model.gallery;

import java.util.List;
import java.util.Optional;

import com.vijay.databox.core.model.User;

public interface VideoChunkRepository {
    VideoChunk save(VideoChunk VideoChunk);

    // Optional<VideoChunk> findById(Long id);

    // List<VideoChunk> allVideoChunks(User user);

    // Optional<VideoChunk> findByIdAndVideoChunkId(Long userId, Long id);

    // void deleteById(Long id);

	// Optional<Integer> findMaxIdentifier(Long id, String fileName);
    Optional<VideoChunk> findByRange(long start, long end, long videoId);

    Optional<VideoChunk> findFirstChunk(long start, long videoId);
}
