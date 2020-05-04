package com.jacky.executors.debug;

import java.io.File;

public class FileUtil {

    public static String analyzeFile(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("file is null or not exist");
        }

        final StringBuilder stringBuilder = new StringBuilder("Analyze file:");
        stringBuilder.append(file.getAbsolutePath()).append("\n")
            .append("isFile:").append(file.isFile()).append("\n");

        if (file.isDirectory()) {
            stringBuilder.append("Dir list files length:")
                .append(file.listFiles().length).append("\n");
        }

        return stringBuilder.toString();
    }
}
