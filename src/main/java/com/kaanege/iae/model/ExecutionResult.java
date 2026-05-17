package com.kaanege.iae.model;

public class ExecutionResult {

    private int id;
    private int projectId;
    private String studentId;
    private String status;
    private String message;
    private int score;

    public ExecutionResult() {
    }

    public ExecutionResult(int projectId, String studentId, String status, String message, int score) {
        this.projectId = projectId;
        this.studentId = studentId;
        this.status = status;
        this.message = message;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}