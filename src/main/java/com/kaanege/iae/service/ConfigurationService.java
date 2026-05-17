package com.kaanege.iae.service;

import com.kaanege.iae.model.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationService {

    private static final String URL = "jdbc:sqlite:iae.db";

    public void saveConfiguration(Configuration configuration) {
        String sql = """
                INSERT INTO configurations(name, language, compile_command, run_command, source_pattern)
                VALUES(?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, configuration.getName());
            pstmt.setString(2, configuration.getLanguage());
            pstmt.setString(3, configuration.getCompileCommand());
            pstmt.setString(4, configuration.getRunCommand());
            pstmt.setString(5, configuration.getSourcePattern());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Configuration> getAllConfigurations() {
        List<Configuration> configurations = new ArrayList<>();
        String sql = "SELECT * FROM configurations ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Configuration configuration = new Configuration();
                configuration.setId(rs.getInt("id"));
                configuration.setName(rs.getString("name"));
                configuration.setLanguage(rs.getString("language"));
                configuration.setCompileCommand(rs.getString("compile_command"));
                configuration.setRunCommand(rs.getString("run_command"));
                configuration.setSourcePattern(rs.getString("source_pattern"));

                configurations.add(configuration);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return configurations;
    }

    public Configuration getById(int id) {
        String sql = "SELECT * FROM configurations WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Configuration(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("language"),
                            rs.getString("compile_command"),
                            rs.getString("run_command"),
                            rs.getString("source_pattern")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateConfiguration(Configuration configuration) {
        String sql = """
                UPDATE configurations
                SET name = ?, language = ?, compile_command = ?, run_command = ?, source_pattern = ?
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, configuration.getName());
            pstmt.setString(2, configuration.getLanguage());
            pstmt.setString(3, configuration.getCompileCommand());
            pstmt.setString(4, configuration.getRunCommand());
            pstmt.setString(5, configuration.getSourcePattern());
            pstmt.setInt(6, configuration.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteConfiguration(int id) {
        String sql = "DELETE FROM configurations WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}