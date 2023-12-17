package com.vijay.databox.api.response;

import java.util.Date;

public record ImageResponse(String name, Long imageId, Date createdAt, String path, String lqPath) {
    public ImageResponse(String name, Long imageId, Date createdAt) {
        this(name, imageId, createdAt, "default/path", "default/lqpath");
    }
    public ImageResponse(String name, Long imageId, Date createdAt, String path) {
        this(name, imageId, createdAt, path, "default/lqpath");
    }

}
