package com.kaanege.iae.service;

import com.kaanege.iae.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private static final String URL = "jdbc:sqlite:iae.db";

    public void saveProject(Project project) {

        String sql = """
                INSERT INTO projects(name, zip_folder, expected_output, configuration_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getZipFolder());
            pstmt.setString(3, project.getExpectedOutputFile());
            pstmt.setInt(4, project.getConfigurationId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Project project = new Project();

                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setZipFolder(rs.getString("zip_folder"));
                project.setExpectedOutputFile(rs.getString("expected_output"));
                project.setConfigurationId(rs.getInt("configuration_id"));

                projects.add(project);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return projects;
    }
    public Project getLatestProject() {
        String sql = "SELECT * FROM projects ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setZipFolder(rs.getString("zip_folder"));
                project.setExpectedOutputFile(rs.getString("expected_output"));
                project.setConfigurationId(rs.getInt("configuration_id"));
                return project;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void updateProject(Project project) {
        String sql = """
            UPDATE projects
            SET name = ?, zip_folder = ?, expected_output = ?, configuration_id = ?
            WHERE id = ?
            """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getZipFolder());
            pstmt.setString(3, project.getExpectedOutputFile());
            pstmt.setInt(4, project.getConfigurationId());
            pstmt.setInt(5, project.getId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Project getById(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setZipFolder(rs.getString("zip_folder"));
                    project.setExpectedOutputFile(rs.getString("expected_output"));
                    project.setConfigurationId(rs.getInt("configuration_id"));
                    return project;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}