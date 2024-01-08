package com.vijay.databox.core.model.gallery;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vijay.databox.core.model.User;

import lombok.Getter;
import lombok.Setter;

@Table(name = "videos")
@Entity
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;
    @Column(nullable = false)
    private int identifier;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private Long videoId;
    @Column(length = 30)
    private String videoType;
    @Column(nullable = true)
    private String[] chunks = new String[]{};
    private long totalSize;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Date createdAt = new Date();
    private Date updatedAt;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + identifier;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((videoId == null) ? 0 : videoId.hashCode());
        result = prime * result + ((videoType == null) ? 0 : videoType.hashCode());
        result = prime * result + Arrays.hashCode(chunks);
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Video other = (Video) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (identifier != other.identifier)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (videoId == null) {
            if (other.videoId != null)
                return false;
        } else if (!videoId.equals(other.videoId))
            return false;
        if (videoType == null) {
            if (other.videoType != null)
                return false;
        } else if (!videoType.equals(other.videoType))
            return false;
        if (!Arrays.equals(chunks, other.chunks))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        return true;
    }
    public Video(String name, int identifier, String path, Long videoId, String videoType, User user, Date updatedAt) {
        this.name = name;
        this.identifier = identifier;
        this.path = path;
        this.videoId = videoId;
        this.videoType = videoType;
        this.user = user;
        this.updatedAt = updatedAt;
    }
    public Video(String name, int identifier, String path, Long videoId, String videoType, User user, Date updatedAt, long totalSize) {
        this.name = name;
        this.identifier = identifier;
        this.path = path;
        this.videoId = videoId;
        this.videoType = videoType;
        this.user = user;
        this.updatedAt = updatedAt;
        this.totalSize = totalSize;
    }
    public Video(Long id, String name, int identifier, String path, Long videoId, String videoType, String[] chunks,
            User user, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.path = path;
        this.videoId = videoId;
        this.videoType = videoType;
        this.chunks = chunks;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Video(){}
    
}
