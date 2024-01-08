package com.vijay.databox.persistence.gallery;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vijay.databox.core.model.gallery.VideoChunk;
import com.vijay.databox.core.model.gallery.VideoChunkRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VideoChunkRepositoryAdapter implements VideoChunkRepository {
    @Autowired
    private VideoChunkJpaRepository videoChunkJpaRepository;
    public VideoChunk save(VideoChunk VideoChunk) {
        return videoChunkJpaRepository.save(VideoChunk);
    }

    public Optional<VideoChunk> findByRange(long start, long end, long videoId) {
        return videoChunkJpaRepository.findByRange(start, videoId);
    }

    public Optional<VideoChunk> findFirstChunk(long start, long videoId) {
        return videoChunkJpaRepository.findFirstChunk(start, videoId);
    }
}
