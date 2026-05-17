package com.kaanege.iae.ui;

import com.kaanege.iae.model.Configuration;
import com.kaanege.iae.model.ExecutionResult;
import com.kaanege.iae.model.Project;
import com.kaanege.iae.service.CompareService;
import com.kaanege.iae.service.CompilerService;
import com.kaanege.iae.service.ConfigurationService;
import com.kaanege.iae.service.ProjectService;
import com.kaanege.iae.service.ResultService;
import com.kaanege.iae.service.RunnerService;
import com.kaanege.iae.service.SubmissionService;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProjectView {

    private final ProjectService projectService = new ProjectService();
    private final ConfigurationService configurationService = new ConfigurationService();
    private final ZipService zipService = new ZipService();
    private final SubmissionService submissionService = new SubmissionService();
    private final CompilerService compilerService = new CompilerService();
    private final RunnerService runnerService = new RunnerService();
    private final CompareService compareService = new CompareService();
    private final ResultService resultService = new ResultService();

    private Project currentProject;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Project Management");

        VBox page = new VBox(18);
        page.setPadding(new Insets(22));
        page.getStyleClass().add("app-root");

        VBox headerCard = createCard();
        Label title = new Label("Project Management");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label(
                "Create a project first, then process submissions step by step using the selected configuration."
        );
        subtitle.getStyleClass().add("subtitle-label");
        subtitle.setWrapText(true);

        headerCard.getChildren().addAll(title, subtitle);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter project name");

        TextField zipField = new TextField();
        zipField.setPromptText("Select ZIP folder path");

        TextField expectedField = new TextField();
        expectedField.setPromptText("Select expected output file");

        Button browseZipBtn = new Button("Browse Folder");
        Button browseExpectedBtn = new Button("Browse File");

        ComboBox<String> configCombo = new ComboBox<>();
        configCombo.setPromptText("Select configuration");
        configCombo.setMaxWidth(Double.MAX_VALUE);
        loadConfigurations(configCombo);

        Button saveBtn = new Button("Create Project");
        saveBtn.getStyleClass().add("primary-button");

        VBox setupCard = createCard();
        setupCard.setPrefWidth(430);

        Label setupTitle = new Label("Project Details");
        setupTitle.getStyleClass().add("section-label");

        Label setupInfo = new Label(
                "Define the project information and choose the grading configuration."
        );
        setupInfo.getStyleClass().add("subtitle-label");
        setupInfo.setWrapText(true);

        GridPane setupForm = new GridPane();
        setupForm.setHgap(12);
        setupForm.setVgap(14);

        setupForm.add(new Label("Project Name"), 0, 0);
        setupForm.add(nameField, 1, 0);

        setupForm.add(new Label("ZIP Folder"), 0, 1);
        setupForm.add(zipField, 1, 1);
        setupForm.add(browseZipBtn, 2, 1);

        setupForm.add(new Label("Expected Output"), 0, 2);
        setupForm.add(expectedField, 1, 2);
        setupForm.add(browseExpectedBtn, 2, 2);

        setupForm.add(new Label("Configuration"), 0, 3);
        setupForm.add(configCombo, 1, 3);

        setupCard.getChildren().addAll(setupTitle, setupInfo, setupForm, saveBtn);

        Button scanBtn = new Button("1. Scan ZIP Folder");
        Button extractBtn = new Button("2. Extract All ZIPs");
        Button analyzeBtn = new Button("3. Analyze Submissions");
        Button compileBtn = new Button("4. Compile Submissions");
        Button gradeBtn = new Button("5. Run and Compare");

        scanBtn.getStyleClass().add("workflow-button");
        extractBtn.getStyleClass().add("workflow-button");
        analyzeBtn.getStyleClass().add("workflow-button");
        compileBtn.getStyleClass().add("workflow-button");
        gradeBtn.getStyleClass().addAll("workflow-button", "primary-button");

        scanBtn.setMaxWidth(Double.MAX_VALUE);
        extractBtn.setMaxWidth(Double.MAX_VALUE);
        analyzeBtn.setMaxWidth(Double.MAX_VALUE);
        compileBtn.setMaxWidth(Double.MAX_VALUE);
        gradeBtn.setMaxWidth(Double.MAX_VALUE);

        workflowButtonSize(scanBtn, extractBtn, analyzeBtn, compileBtn, gradeBtn);

        VBox workflowCard = createCard();
        HBox.setHgrow(workflowCard, Priority.ALWAYS);

        Label workflowTitle = new Label("Processing Workflow");
        workflowTitle.getStyleClass().add("section-label");

        Label workflowInfo = new Label(
                "Use these actions in order after creating a project. Each step updates the activity log below."
        );
        workflowInfo.getStyleClass().add("subtitle-label");
        workflowInfo.setWrapText(true);

        Label statusLabel = new Label("Current Step: Waiting for project creation");
        statusLabel.getStyleClass().add("subtitle-label");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(10);

        GridPane workflowGrid = new GridPane();
        workflowGrid.setHgap(12);
        workflowGrid.setVgap(12);

        workflowGrid.add(scanBtn, 0, 0);
        workflowGrid.add(extractBtn, 1, 0);
        workflowGrid.add(analyzeBtn, 0, 1);
        workflowGrid.add(compileBtn, 1, 1);
        workflowGrid.add(gradeBtn, 0, 2, 2, 1);

        workflowCard.getChildren().addAll(workflowTitle, workflowInfo, statusLabel, progressBar, workflowGrid);

        HBox topRow = new HBox(18, setupCard, workflowCard);

        VBox logCard = createCard();
        VBox.setVgrow(logCard, Priority.ALWAYS);

        Label logTitle = new Label("Activity Log");
        logTitle.getStyleClass().add("section-label");

        Label logInfo = new Label(
                "Scan results, extraction output, source detection, compile messages and grading results will appear here."
        );
        logInfo.getStyleClass().add("subtitle-label");
        logInfo.setWrapText(true);

        ListView<String> zipListView = new ListView<>();
        zipListView.setPrefHeight(360);
        VBox.setVgrow(zipListView, Priority.ALWAYS);

        zipListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(item);

                if (item.contains("PASS")) {
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else if (item.contains("FAIL")) {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else if (item.contains("ERROR")) {
                    setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                } else if (item.contains("SUCCESS")) {
                    setStyle("-fx-text-fill: green;");
                } else if (item.contains("FAILED")) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });

        logCard.getChildren().addAll(logTitle, logInfo, zipListView);

        page.getChildren().addAll(headerCard, topRow, logCard);

        saveBtn.setOnAction(e -> {
            String projectName = nameField.getText().trim();
            String zipPath = zipField.getText().trim();
            String expectedPath = expectedField.getText().trim();
            String selectedConfig = configCombo.getValue();

            if (projectName.isEmpty()) {
                showWarning("Missing Project Name", "Please enter a project name.");
                return;
            }

            if (zipPath.isEmpty()) {
                showWarning("Missing ZIP Folder", "Please select or enter the ZIP folder path.");
                return;
            }

            if (expectedPath.isEmpty()) {
                showWarning("Missing Expected Output", "Please select or enter the expected output file.");
                return;
            }

            if (selectedConfig == null) {
                showWarning("Missing Configuration", "Please select a configuration.");
                return;
            }

            File zipFolder = new File(zipPath);
            if (!zipFolder.exists() || !zipFolder.isDirectory()) {
                showWarning("Invalid ZIP Folder", "The ZIP folder path does not exist or is not a folder.");
                return;
            }

            File expectedFile = new File(expectedPath);
            if (!expectedFile.exists() || !expectedFile.isFile()) {
                showWarning("Invalid Expected Output File", "The expected output file does not exist.");
                return;
            }

            int configId = Integer.parseInt(selectedConfig.split(" - ")[0]);

            Project project = new Project(
                    projectName,
                    zipPath,
                    expectedPath,
                    configId
            );

            projectService.saveProject(project);
            currentProject = projectService.getLatestProject();

            zipListView.getItems().add("Project created: " + projectName);
            statusLabel.setText("Current Step: Project created");
            progressBar.setProgress(0.10);

            showInfo("Success", "Project created successfully.");
        });

        browseZipBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select ZIP Folder");

            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                zipField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        browseExpectedBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Expected Output File");

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                expectedField.setText(selectedFile.getAbsolutePath());
            }
        });

        scanBtn.setOnAction(e -> {
            zipListView.getItems().clear();
            statusLabel.setText("Current Step: Scanning ZIP folder");
            progressBar.setProgress(0.25);

            var zipFiles = zipService.getZipFiles(zipField.getText().trim());
            var folders = zipService.getSubmissionFolders(zipField.getText().trim());

            for (File file : zipFiles) {
                zipListView.getItems().add("[ZIP] " + file.getName());
            }

            for (File folder : folders) {
                zipListView.getItems().add("[FOLDER] " + folder.getName());
            }

            if (zipFiles.isEmpty() && folders.isEmpty()) {
                zipListView.getItems().add("No ZIP files or folders found.");
            }
        });

        extractBtn.setOnAction(e -> {
            String zipFolderPath = zipField.getText().trim();

            if (zipFolderPath.isEmpty()) {
                showWarning("Missing Folder Path", "Please enter the ZIP folder path first.");
                return;
            }

            try {
                statusLabel.setText("Current Step: Extracting ZIP files");
                progressBar.setProgress(0.40);

                String outputRootPath = "extracted";
                var extractedFolders = zipService.extractAllZips(zipFolderPath, outputRootPath);

                zipListView.getItems().clear();

                if (extractedFolders.isEmpty()) {
                    zipListView.getItems().add("No real ZIP files found to extract.");
                } else {
                    for (File folder : extractedFolders) {
                        zipListView.getItems().add("Extracted: " + folder.getPath());
                    }
                }

                showInfo("Extraction Complete", "Real ZIP files were extracted into the 'extracted' folder.");

            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Extraction Error", ex.getMessage());
            }
        });

        analyzeBtn.setOnAction(e -> {
            zipListView.getItems().clear();
            statusLabel.setText("Current Step: Analyzing submissions");
            progressBar.setProgress(0.55);

            Configuration config = getSelectedConfiguration(configCombo);
            var folders = submissionService.getSubmissionFolders(zipField.getText().trim());

            if (folders.isEmpty()) {
                zipListView.getItems().add("No folders found.");
                return;
            }

            for (File folder : folders) {
                File source;

                if (config != null && config.getSourcePattern() != null && !config.getSourcePattern().isBlank()) {
                    source = submissionService.findSourceFileByPattern(folder, config.getSourcePattern());
                } else {
                    source = submissionService.findSourceFile(folder);
                }

                if (source != null) {
                    zipListView.getItems().add(folder.getName() + " -> " + source.getName());
                } else {
                    if (config != null && config.getSourcePattern() != null && !config.getSourcePattern().isBlank()) {
                        zipListView.getItems().add(folder.getName() + " -> No source file matching " + config.getSourcePattern());
                    } else {
                        zipListView.getItems().add(folder.getName() + " -> No source file");
                    }
                }
            }
        });

        compileBtn.setOnAction(e -> {
            zipListView.getItems().clear();
            statusLabel.setText("Current Step: Compiling submissions");
            progressBar.setProgress(0.75);

            if (currentProject == null) {
                zipListView.getItems().add("Create project first.");
                return;
            }

            Configuration config = configurationService.getById(currentProject.getConfigurationId());

            if (config == null) {
                zipListView.getItems().add("Configuration not found.");
                return;
            }

            var folders = submissionService.getSubmissionFolders(zipField.getText().trim());

            if (folders.isEmpty()) {
                zipListView.getItems().add("No folders found.");
                return;
            }

            for (File folder : folders) {
                File sourceFile = submissionService.findSourceFileByPattern(folder, config.getSourcePattern());

                if (sourceFile == null) {
                    zipListView.getItems().add(folder.getName() + " -> No matching source file");
                    continue;
                }

                String finalCommand = buildCommand(config.getCompileCommand(), sourceFile);

                if (finalCommand.isBlank()) {
                    zipListView.getItems().add(folder.getName() + " -> Compile command is empty");
                    continue;
                }

                boolean success = compilerService.compile(folder, finalCommand);

                if (success) {
                    zipListView.getItems().add(folder.getName() + " -> SUCCESS | Score Preview: 30");
                } else {
                    zipListView.getItems().add(folder.getName() + " -> FAILED | Score: 0");
                }
            }
        });

        gradeBtn.setOnAction(e -> {
            zipListView.getItems().clear();
            statusLabel.setText("Current Step: Running and comparing");
            progressBar.setProgress(1.0);

            String expectedPath = expectedField.getText().trim();

            if (expectedPath.isEmpty()) {
                showWarning("Missing Expected Output", "Please enter Expected Output File path.");
                return;
            }

            if (currentProject == null) {
                showWarning("No Project", "Please create a project first.");
                return;
            }

            Configuration config = configurationService.getById(currentProject.getConfigurationId());

            if (config == null) {
                zipListView.getItems().add("Configuration not found.");
                return;
            }

            String expectedOutput = compareService.readExpectedOutput(expectedPath);
            resultService.deleteResultsByProjectId(currentProject.getId());

            var folders = submissionService.getSubmissionFolders(zipField.getText().trim());

            if (folders.isEmpty()) {
                zipListView.getItems().add("No folders found.");
                return;
            }

            for (File folder : folders) {
                File sourceFile = submissionService.findSourceFileByPattern(folder, config.getSourcePattern());

                if (sourceFile == null) {
                    int score = calculateScore(false, false, false);

                    zipListView.getItems().add(folder.getName() + " -> COMPILE FAIL | Score: " + score);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "COMPILE FAIL",
                                    "No source file matched pattern: " + config.getSourcePattern(),
                                    score
                            )
                    );
                    continue;
                }

                String finalCompileCommand = buildCommand(config.getCompileCommand(), sourceFile);

                if (finalCompileCommand.isBlank()) {
                    int score = calculateScore(false, false, false);

                    zipListView.getItems().add(folder.getName() + " -> COMPILE FAIL | Score: " + score);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "COMPILE FAIL",
                                    "Compile command is empty.",
                                    score
                            )
                    );
                    continue;
                }

                boolean compileSuccess = compilerService.compile(folder, finalCompileCommand);

                if (!compileSuccess) {
                    int score = calculateScore(false, false, false);

                    zipListView.getItems().add(folder.getName() + " -> COMPILE FAIL | Score: " + score);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "COMPILE FAIL",
                                    "Compilation failed.",
                                    score
                            )
                    );
                    continue;
                }

                String finalRunCommand = buildCommand(config.getRunCommand(), sourceFile);
                String actualOutput = runnerService.runCommand(folder, finalRunCommand);

                if (actualOutput == null) {
                    int score = calculateScore(true, false, false);

                    zipListView.getItems().add(folder.getName() + " -> RUN ERROR | Score: " + score);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "RUN ERROR",
                                    "Program could not be executed.",
                                    score
                            )
                    );
                    continue;
                }

                boolean match = compareService.isMatch(actualOutput, expectedOutput);

                if (match) {
                    int score = calculateScore(true, true, true);

                    zipListView.getItems().add(folder.getName() + " -> PASS | Score: " + score);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "PASS",
                                    actualOutput,
                                    score
                            )
                    );
                } else {
                    int score = calculateScore(true, true, false);

                    zipListView.getItems().add(folder.getName() + " -> FAIL | Score: " + score + " | Actual: " + actualOutput);

                    resultService.saveResult(
                            new ExecutionResult(
                                    currentProject.getId(),
                                    folder.getName(),
                                    "FAIL",
                                    actualOutput,
                                    score
                            )
                    );
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(1180, bounds.getWidth() - 100);
        double height = Math.min(820, bounds.getHeight() - 100);

        Scene scene = new Scene(scrollPane, width, height);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(720);
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());
        stage.centerOnScreen();
        stage.show();
    }

    private VBox createCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card");
        return card;
    }

    private void workflowButtonSize(Button... buttons) {
        for (Button button : buttons) {
            button.setPrefHeight(42);
        }
    }

    private void loadConfigurations(ComboBox<String> configCombo) {
        configCombo.getItems().clear();

        List<Configuration> configurations = configurationService.getAllConfigurations();
        for (Configuration c : configurations) {
            configCombo.getItems().add(c.getId() + " - " + c.getName());
        }
    }

    private Configuration getSelectedConfiguration(ComboBox<String> configCombo) {
        String selectedConfig = configCombo.getValue();

        if (selectedConfig == null || !selectedConfig.contains(" - ")) {
            return null;
        }

        try {
            int configId = Integer.parseInt(selectedConfig.split(" - ")[0]);
            return configurationService.getById(configId);
        } catch (Exception e) {
            return null;
        }
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int calculateScore(boolean compileSuccess, boolean runSuccess, boolean outputMatch) {
        int score = 0;

        if (compileSuccess) {
            score += 30;
        }

        if (runSuccess) {
            score += 20;
        }

        if (outputMatch) {
            score += 50;
        }

        return score;
    }

    private String buildCommand(String command, File sourceFile) {
        if (command == null || command.isBlank()) {
            return "";
        }

        if (sourceFile == null) {
            return command;
        }

        String sourceName = sourceFile.getName();
        String sourceNameNoExt = removeExtension(sourceName);
        String mainClass = sourceNameNoExt;
        String sourcePath = sourceFile.getAbsolutePath();

        return command
                .replace("{source}", "\"" + sourceName + "\"")
                .replace("{sourceName}", sourceName)
                .replace("{sourcePath}", "\"" + sourcePath + "\"")
                .replace("{sourceNameNoExt}", sourceNameNoExt)
                .replace("{mainClass}", mainClass);
    }

    private String removeExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }

        return fileName;
    }
}