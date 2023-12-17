package com.vijay.databox.core.model.gallery;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Table(name = "gallery_images")
@Entity
@Getter
public class Image {
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
    private Long imageId;
    @Column(length = 30)
    private String imageType;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Date createdAt = new Date();
    private Date updatedAt;

    public Image(String name, String path, Long imageId, String imageType, User user, Date updatedAt, int identifier) {
        super();
        this.name = name;
        this.path = path;
        this.imageId = imageId;
        this.imageType = imageType;
        this.user = user;
        this.updatedAt = updatedAt;
        this.identifier = identifier;
    }

    public Image(String name, String path, Long imageId, String imageType, User user, Date updatedAt) {
        super();
        this.name = name;
        this.path = path;
        this.imageId = imageId;
        this.imageType = imageType;
        this.user = user;
        this.updatedAt = updatedAt;
        this.identifier = 0;
    }

    public Image() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((imageType == null) ? 0 : imageType.hashCode());
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
        Image other = (Image) obj;
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
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (imageType == null) {
            if (other.imageType != null)
                return false;
        } else if (!imageType.equals(other.imageType))
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

    public Long getImageId() {
        return this.imageId;
    }
    public int getIdentifier() {
        return this.identifier;
    }
}
