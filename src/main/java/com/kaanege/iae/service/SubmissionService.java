package com.kaanege.iae.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubmissionService {

    public List<File> getSubmissionFolders(String path) {
        List<File> folders = new ArrayList<>();

        File root = new File(path);

        if (!root.exists() || !root.isDirectory()) {
            return folders;
        }

        File[] files = root.listFiles();

        if (files == null) {
            return folders;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                folders.add(file);
            }
        }

        return folders;
    }

    public File findSourceFile(File folder) {
        return search(folder);
    }

    private File search(File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        if (file.isFile()) {
            String name = file.getName().toLowerCase();

            if (name.endsWith(".c")
                    || name.endsWith(".cpp")
                    || name.endsWith(".java")
                    || name.endsWith(".py")) {
                return file;
            }
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children != null) {
                for (File child : children) {
                    File found = search(child);

                    if (found != null) {
                        return found;
                    }
                }
            }
        }

        return null;
    }

    public String findFirstSourceFileName(File folder, String extension) {
        File file = findFirstSourceFile(folder, extension);
        return file != null ? file.getName() : null;
    }

    public File findFirstSourceFile(File folder, String extension) {
        if (folder == null || !folder.exists() || extension == null || extension.isBlank()) {
            return null;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return null;
        }

        String normalizedExtension = extension.toLowerCase();

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(normalizedExtension)) {
                return file;
            }
        }

        for (File file : files) {
            if (file.isDirectory()) {
                File found = findFirstSourceFile(file, normalizedExtension);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    public List<File> findAllFilesRecursively(File folder) {
        List<File> files = new ArrayList<>();
        collectFiles(folder, files);
        return files;
    }

    private void collectFiles(File file, List<File> files) {
        if (file == null || !file.exists()) {
            return;
        }

        if (file.isFile()) {
            files.add(file);
            return;
        }

        File[] children = file.listFiles();
        if (children == null) {
            return;
        }

        for (File child : children) {
            collectFiles(child, files);
        }
    }

    public File findSourceFileByPattern(File folder, String sourcePattern) {
        List<File> allFiles = findAllFilesRecursively(folder);

        if (allFiles.isEmpty()) {
            return null;
        }

        if (sourcePattern == null || sourcePattern.isBlank()) {
            return findSourceFile(folder);
        }

        String pattern = sourcePattern.trim().toLowerCase();

        for (File file : allFiles) {
            String fileName = file.getName().toLowerCase();

            if (matchesPattern(fileName, pattern)) {
                return file;
            }
        }

        return null;
    }

    private boolean matchesPattern(String fileName, String pattern) {
        if (pattern == null || pattern.isBlank()) {
            return false;
        }

        pattern = pattern.trim().toLowerCase();

        if (pattern.startsWith("*.")) {
            String extension = pattern.substring(1); // örn: ".c", ".java"
            return fileName.endsWith(extension);
        }

        return fileName.equals(pattern);
    }
}