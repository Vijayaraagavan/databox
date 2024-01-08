package com.vijay.databox.core.model.gallery;

public record VideoDetails(String name, String type, int index, long size, long total, long videoId, long chunkStart, long chunkEnd) {}
