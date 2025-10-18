package com.example.cinema_management.movie.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;


@Service
public class FileSystemPosterStorageService implements PosterStorageService {
    private final Path root;
    public FileSystemPosterStorageService(@Value("${app.media.posters-dir:uploads/posters}") String dir) {
        this.root = Paths.get(dir).toAbsolutePath().normalize();
        try { Files.createDirectories(root); } catch (IOException e) { throw new RuntimeException(e); }
    }
    @Override
    public String storePoster(MultipartFile file, Long movieId) {
        if (file == null || file.isEmpty()) return null;
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String safe = "m" + movieId + "_" + UUID.randomUUID() + "_" + Instant.now().toEpochMilli()
                + (ext != null ? "." + ext.toLowerCase() : "");
        Path target = root.resolve(safe);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { throw new RuntimeException("Failed to store poster", e); }
        return safe;
    }
    @Override
    public void deletePosterIfExists(String path) {
        if (path == null || path.isBlank()) return;
        try { Files.deleteIfExists(root.resolve(path)); } catch (IOException ignored) {}
    }
}