package com.kaanege.iae.service;

import java.io.File;

public class CompilerService {

    public boolean compile(File folder, String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);

            pb.directory(folder);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            process.waitFor();

            return process.exitValue() == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}