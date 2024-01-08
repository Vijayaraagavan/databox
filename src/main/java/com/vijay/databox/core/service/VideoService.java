package com.vijay.databox.core.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.databox.api.response.VideoStreamResponse;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRepository;
import com.vijay.databox.core.model.gallery.ImageDetails;
import com.vijay.databox.core.model.gallery.Video;
import com.vijay.databox.core.model.gallery.VideoChunk;
import com.vijay.databox.core.model.gallery.VideoChunkRepository;
import com.vijay.databox.core.model.gallery.VideoDetails;
import com.vijay.databox.core.model.gallery.VideoRepository;
import com.vijay.databox.core.modules.Range;
import com.vijay.databox.exception.CustomException;

@Service
public class VideoService {
    @Autowired
    private AtomicLong counter;
    @Value("${storage.path}")
    private String location;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private VideoChunkRepository videoChunkRepo;

    public Video saveVideo(MultipartFile file, VideoDetails details, UserJwtDetails userJwtDetails) {
        User user = userRepo.findByUserName(userJwtDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        long uid = generateId(details.videoId());
        String fileType = details.type().split("/")[1];
        String fileName = String.format("%d_%d.%s", uid, details.index(), fileType);
        String filePath = saveToStorage(fileName, file, uid);
        int identifier = fetchName(user, details);
        Video existVideo = videoRepo.findByIdAndVideoId(user.getId(), uid).orElse(null);
        Video updated;
        if (existVideo != null) {
            updated = updateChunk(existVideo, fileName, details);
        } else {
            Video video = new Video(details.name(), identifier, filePath, uid, fileType, user, new Date(), details.total());
            String[] chunk = new String[] { fileName };
            video.setChunks(chunk);
            updated = videoRepo.save(video);
            updated = saveChunk(video, fileName, details);
        }
        // System.out.println("chunks: " + Arrays.toString(updated.getChunks()));
        return updated;
    }

    public Video getVideo(long id) {
        Video video = videoRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("video not found"));
        // new FileSystemRe
        String dir = location + "/videos/";
        // return dir + "1703096668771/lake_video.mp4";
        // return "/Users/vijay/Downloads/Jawan.mkv";
        return video;
    }

    public VideoStreamResponse getChunk(Range range, Long id) {
        VideoChunk chunk;
        // if (range.getEnd() == Long.MAX_VALUE) {
        //     chunk = getFirstChunk(range, id);
        //     range.setEnd(chunk.getChunkEnd());
        // } else {
            chunk = videoChunkRepo.findByRange(range.getStart(), range.getEnd(), id).orElseThrow(() -> new IllegalArgumentException("video not found"));
        // }
        Video video = videoRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("video not found"));
        long start = range.getStart() - chunk.getChunkStart();
        long end = range.getEnd() - chunk.getChunkStart();
        end = Math.min(end, chunk.getChunkEnd());
        range.setEnd(end);
        String sourcePath = video.getPath() + "/" + chunk.getFileName();
        String destPath = video.getPath() + "/main";
        chunkTransfer( sourcePath,  destPath,  start,  end);
        return new VideoStreamResponse(video.getTotalSize(), destPath);
    }

    private VideoChunk getFirstChunk(Range range, Long id) {
        return videoChunkRepo.findFirstChunk(range.getStart(), id).orElseThrow(() -> new IllegalArgumentException("video not found"));
    }

    private void chunkTransfer(String sourcePath, String destPath, long start, long end) {
                long size = end - start;
        try {
            RandomAccessFile source = new RandomAccessFile(sourcePath, "r");
            RandomAccessFile dest = new RandomAccessFile(destPath, "rw");
            source.seek(start);
            byte[] buffer = new byte[4096];
            long bytesRead = 0;
            int read;
            while ((bytesRead < size) && (read = source.read(buffer)) != -1) {
                int bytesToWrite = (int) Math.min(read, size - bytesRead);
                dest.write(buffer, 0, bytesToWrite);
                bytesRead += bytesToWrite;
            }
            System.out.println("bytes read: " + bytesRead);
            source.close();
            dest.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            // TODO: handle exception
        }
    }

    public long generateId(long id) {
        if (id != -1) {
            return id;
        }
        long random = (long) (Math.random() * 1000);
        return counter.getAndIncrement() + random;
    }

    public String saveToStorage(String name, MultipartFile file, long uid) {
        String dir = location + "/videos/" + uid;
        String filePath = dir + "/" + name;
        createDir(dir);
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new CustomException("Error parsing file");
        }
        return dir;
    }

    private Video updateChunk(Video existVideo, String fileName, VideoDetails details) {
        String[] chunks = existVideo.getChunks();
        String[] newChunks = new String[chunks.length + 1];
        for (int i = 0; i < chunks.length; i++) {
            newChunks[i] = chunks[i];
        }
        newChunks[newChunks.length - 1] = fileName;
        existVideo.setChunks(newChunks);
        existVideo.setUpdatedAt(new Date());
        // chunk entry
        VideoChunk videoChunk = new VideoChunk(existVideo.getId(), details.chunkStart(), details.chunkEnd(),
                details.index(), fileName);
        videoChunkRepo.save(videoChunk);
        return videoRepo.save(existVideo);
    }
    private Video saveChunk(Video existVideo, String fileName, VideoDetails details) {
        VideoChunk videoChunk = new VideoChunk(existVideo.getId(), details.chunkStart(), details.chunkEnd(),
                details.index(), fileName);
        videoChunkRepo.save(videoChunk);
        return existVideo;
    }

    private void createDir(String dir) {
        Path path = Paths.get(dir);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + dir);
            } catch (IOException e) {
                System.err.println("Error creating directory: " + e.getMessage());
            }
        }
    }

    public int fetchName(User user, VideoDetails details) {
        Integer identifier = videoRepo.findMaxIdentifier(user.getId(), details.name()).orElse(null);
        if (identifier == null) {
            return 0;
        } else {
            return identifier + 1;
        }
    }
}
