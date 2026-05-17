package com.kaanege.iae.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:iae.db";

    public static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS configurations(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        language TEXT,
                        compile_command TEXT,
                        run_command TEXT,
                        source_pattern TEXT
                    )
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS projects(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        zip_folder TEXT,
                        expected_output TEXT,
                        configuration_id INTEGER
                    )
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS results(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        project_id INTEGER,
                        student_id TEXT,
                        status TEXT,
                        message TEXT,
                        score INTEGER DEFAULT 0
                    )
                """);

                try {
                    stmt.execute("ALTER TABLE configurations ADD COLUMN source_pattern TEXT");
                } catch (Exception ignored) {
                }

                try {
                    stmt.execute("ALTER TABLE results ADD COLUMN score INTEGER DEFAULT 0");
                } catch (Exception ignored) {
                }

                System.out.println("Database ready.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}