package org.studyeasy.SpringStarterMVCProject.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppUtil {
    public static String getUploadPath(String fileName) {
        Path uploadDir = Paths.get("uploads");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadDir.resolve(fileName).toAbsolutePath().toString();
    }
}
