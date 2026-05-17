package com.kaanege.iae.model;

public class Configuration {

    private int id;
    private String name;
    private String language;
    private String compileCommand;
    private String runCommand;
    private String sourcePattern;

    public Configuration() {
    }

    public Configuration(String name, String language, String compileCommand, String runCommand, String sourcePattern) {
        this.name = name;
        this.language = language;
        this.compileCommand = compileCommand;
        this.runCommand = runCommand;
        this.sourcePattern = sourcePattern;
    }

    public Configuration(int id, String name, String language, String compileCommand, String runCommand, String sourcePattern) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.compileCommand = compileCommand;
        this.runCommand = runCommand;
        this.sourcePattern = sourcePattern;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCompileCommand() {
        return compileCommand;
    }

    public void setCompileCommand(String compileCommand) {
        this.compileCommand = compileCommand;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getSourcePattern() {
        return sourcePattern;
    }

    public void setSourcePattern(String sourcePattern) {
        this.sourcePattern = sourcePattern;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", compileCommand='" + compileCommand + '\'' +
                ", runCommand='" + runCommand + '\'' +
                ", sourcePattern='" + sourcePattern + '\'' +
                '}';
    }
}