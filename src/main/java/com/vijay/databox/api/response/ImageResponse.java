package com.vijay.databox.api.response;

import java.util.Date;

public record ImageResponse(String name, Long imageId, Date createdAt) {
    
}
