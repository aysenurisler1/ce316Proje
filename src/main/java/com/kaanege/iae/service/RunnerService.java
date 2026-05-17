package com.kaanege.iae.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class RunnerService {

    public String runCommand(File folder, String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
            pb.directory(folder);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            process.waitFor();

            if (process.exitValue() != 0) {
                return null;
            }

            return output.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}