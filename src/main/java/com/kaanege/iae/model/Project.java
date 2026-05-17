package com.kaanege.iae.model;

public class Project {

    private int id;
    private String name;
    private String zipFolder;
    private String expectedOutputFile;
    private int configurationId;

    public Project() {
    }

    public Project(String name, String zipFolder,
                   String expectedOutputFile,
                   int configurationId) {
        this.name = name;
        this.zipFolder = zipFolder;
        this.expectedOutputFile = expectedOutputFile;
        this.configurationId = configurationId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getZipFolder() { return zipFolder; }
    public void setZipFolder(String zipFolder) { this.zipFolder = zipFolder; }

    public String getExpectedOutputFile() { return expectedOutputFile; }
    public void setExpectedOutputFile(String expectedOutputFile) { this.expectedOutputFile = expectedOutputFile; }

    public int getConfigurationId() { return configurationId; }
    public void setConfigurationId(int configurationId) { this.configurationId = configurationId; }
    @Override
    public String toString() {
        return "ID: " + id + " | " + name;
    }
}