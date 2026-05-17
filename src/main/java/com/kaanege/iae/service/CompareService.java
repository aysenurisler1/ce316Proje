package com.kaanege.iae.service;

import java.io.File;
import java.nio.file.Files;

public class CompareService {

    public String readExpectedOutput(String filePath) {
        try {
            return Files.readString(new File(filePath).toPath()).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isMatch(String actualOutput, String expectedOutput) {
        if (actualOutput == null || expectedOutput == null) {
            return false;
        }

        return actualOutput.trim().equals(expectedOutput.trim());
    }
}