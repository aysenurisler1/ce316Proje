package com.kaanege.iae.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipService {

    public List<File> getZipFiles(String folderPath) {
        List<File> zipFiles = new ArrayList<>();

        folderPath = folderPath.trim();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return zipFiles;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return zipFiles;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
                zipFiles.add(file);
            }
        }

        return zipFiles;
    }

    public List<File> getSubmissionFolders(String folderPath) {
        List<File> folders = new ArrayList<>();

        folderPath = folderPath.trim();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return folders;
        }

        File[] files = folder.listFiles();
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

    public File extractZip(File zipFile, String outputRootPath) throws IOException {
        String zipName = zipFile.getName();
        String folderName = zipName.substring(0, zipName.lastIndexOf('.'));

        File outputDir = new File(outputRootPath, folderName);
        if (!outputDir.exists()) {
            Files.createDirectories(outputDir.toPath());
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                File newFile = newFile(outputDir, entry);

                if (entry.isDirectory()) {
                    Files.createDirectories(newFile.toPath());
                } else {
                    File parent = newFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        Files.createDirectories(parent.toPath());
                    }

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }

                zis.closeEntry();
            }
        }

        return outputDir;
    }

    public List<File> extractAllZips(String zipFolderPath, String outputRootPath) throws IOException {
        List<File> extractedFolders = new ArrayList<>();
        List<File> zipFiles = getZipFiles(zipFolderPath);

        for (File zipFile : zipFiles) {
            File extractedFolder = extractZip(zipFile, outputRootPath);
            extractedFolders.add(extractedFolder);
        }

        return extractedFolders;
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator) &&
                !destFilePath.equals(destDirPath)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}