package com.kaanege.iae.service;

import com.kaanege.iae.model.ExecutionResult;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CsvExportService {

    public boolean exportResults(File file, List<ExecutionResult> results) {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("Project ID;Student ID;Status;Score;Output Summary\n");

            for (ExecutionResult result : results) {
                writer.write(result.getProjectId() + ";"
                        + escape(result.getStudentId()) + ";"
                        + escape(result.getStatus()) + ";"
                        + result.getScore() + ";"
                        + escape(makeSummary(result.getMessage())) + "\n");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String makeSummary(String value) {
        if (value == null) {
            return "";
        }

        String trimmed = value.trim().replace("\n", " ").replace("\r", " ");
        if (trimmed.length() > 60) {
            return trimmed.substring(0, 60) + "...";
        }
        return trimmed;
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}