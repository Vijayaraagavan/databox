package com.vijay.databox.core.modules;

import lombok.Setter;

@Setter
public class Range {

    private long start;
    private long end;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public static Range parseRange(String rangeHeader) {
        // Parse the Range header value
        // Example header: "bytes=100-200"
        String[] rangeValues = rangeHeader.trim().substring("bytes=".length()).split("-");
        long start = Long.parseLong(rangeValues[0]);
        long end = rangeValues.length > 1 ? Long.parseLong(rangeValues[1]) : start + (30 * 1024 * 1024);
        return new Range(start, end);
    }

    public String getContentRange(long totalLength) {
        // Set the Content-Range header value
        return "bytes " + start + "-" + end + "/" + totalLength;
    }

    public long getStart() {
        return start;
    }

    @Override
    public String toString() {
        return "Range [start=" + start + ", end=" + end + "]";
    }

    public long getEnd() {
        return end;
    }
}

