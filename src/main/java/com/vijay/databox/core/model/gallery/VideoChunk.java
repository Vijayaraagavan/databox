package com.vijay.databox.core.model.gallery;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_chunks")
@Getter
@Setter
public class VideoChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long videoId;
    @Column(nullable = false)
    private Long chunkStart;
    @Column(nullable = false)
    private Long chunkEnd;
    private Integer index = 0;
    private String fileName;
    private Date createdAt = new Date();
    public VideoChunk(Long videoId, Long chunkStart, Long chunkEnd, Integer index, String fileName) {
        this.videoId = videoId;
        this.chunkStart = chunkStart;
        this.chunkEnd = chunkEnd;
        this.index = index;
        this.fileName = fileName;
    }
    public VideoChunk(){}
}
