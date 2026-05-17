package com.kaanege.iae.service;

import com.kaanege.iae.model.ExecutionResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultService {

    private static final String URL = "jdbc:sqlite:iae.db";

    public void saveResult(ExecutionResult result) {
        String sql = """
                INSERT INTO results(project_id, student_id, status, message, score)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, result.getProjectId());
            pstmt.setString(2, result.getStudentId());
            pstmt.setString(3, result.getStatus());
            pstmt.setString(4, result.getMessage());
            pstmt.setInt(5, result.getScore());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ExecutionResult> getResultsByProjectId(int projectId) {
        List<ExecutionResult> results = new ArrayList<>();

        String sql = "SELECT * FROM results WHERE project_id = ? ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ExecutionResult result = new ExecutionResult();
                    result.setId(rs.getInt("id"));
                    result.setProjectId(rs.getInt("project_id"));
                    result.setStudentId(rs.getString("student_id"));
                    result.setStatus(rs.getString("status"));
                    result.setMessage(rs.getString("message"));
                    result.setScore(rs.getInt("score"));
                    results.add(result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<ExecutionResult> getAllResults() {
        List<ExecutionResult> results = new ArrayList<>();

        String sql = "SELECT * FROM results ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ExecutionResult result = new ExecutionResult();
                result.setId(rs.getInt("id"));
                result.setProjectId(rs.getInt("project_id"));
                result.setStudentId(rs.getString("student_id"));
                result.setStatus(rs.getString("status"));
                result.setMessage(rs.getString("message"));
                result.setScore(rs.getInt("score"));
                results.add(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void deleteResultsByProjectId(int projectId) {
        String sql = "DELETE FROM results WHERE project_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}