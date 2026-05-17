package com.kaanege.iae.ui;

import com.kaanege.iae.model.Configuration;
import com.kaanege.iae.model.Project;
import com.kaanege.iae.service.ConfigurationService;
import com.kaanege.iae.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProjectSelectionView {

    private final ProjectService projectService = new ProjectService();
    private final ConfigurationService configurationService = new ConfigurationService();

    private final ListView<String> listView = new ListView<>();
    private Project selectedProject;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Saved Projects");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        Label title = new Label("Saved Projects");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        TextField zipField = new TextField();
        TextField expectedField = new TextField();

        Button browseZipBtn = new Button("Browse Folder");
        Button browseExpectedBtn = new Button("Browse File");

        ComboBox<String> configCombo = new ComboBox<>();
        loadConfigurations(configCombo);

        Button refreshBtn = new Button("Refresh");
        Button updateBtn = new Button("Update Project");
        Button deleteBtn = new Button("Delete Project");
        Button clearBtn = new Button("Clear");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15, 0, 0, 0));

        form.add(new Label("Project Name:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(new Label("ZIP Folder Path:"), 0, 1);
        form.add(zipField, 1, 1);
        form.add(browseZipBtn, 2, 1);

        form.add(new Label("Expected Output File:"), 0, 2);
        form.add(expectedField, 1, 2);
        form.add(browseExpectedBtn, 2, 2);

        form.add(new Label("Configuration:"), 0, 3);
        form.add(configCombo, 1, 3);

        form.add(updateBtn, 0, 4);
        form.add(deleteBtn, 1, 4);
        form.add(clearBtn, 2, 4);
        form.add(refreshBtn, 0, 5);

        loadProjects();

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                return;
            }

            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            List<Project> projects = projectService.getAllProjects();

            if (selectedIndex >= 0 && selectedIndex < projects.size()) {
                selectedProject = projects.get(selectedIndex);

                nameField.setText(selectedProject.getName());
                zipField.setText(selectedProject.getZipFolder());
                expectedField.setText(selectedProject.getExpectedOutputFile());

                String configText = selectedProject.getConfigurationId() + " - " +
                        getConfigurationName(selectedProject.getConfigurationId());
                configCombo.setValue(configText);
            }
        });

        browseZipBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select ZIP Folder");
            File selected = chooser.showDialog(stage);
            if (selected != null) {
                zipField.setText(selected.getAbsolutePath());
            }
        });

        browseExpectedBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Expected Output File");
            File selected = chooser.showOpenDialog(stage);
            if (selected != null) {
                expectedField.setText(selected.getAbsolutePath());
            }
        });

        refreshBtn.setOnAction(e -> {
            loadProjects();
            loadConfigurations(configCombo);
        });

        clearBtn.setOnAction(e -> {
            selectedProject = null;
            listView.getSelectionModel().clearSelection();
            nameField.clear();
            zipField.clear();
            expectedField.clear();
            configCombo.getSelectionModel().clearSelection();
        });

        updateBtn.setOnAction(e -> {
            if (selectedProject == null) {
                showWarning("No Selection", "Please select a project to update.");
                return;
            }

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

            selectedProject.setName(projectName);
            selectedProject.setZipFolder(zipPath);
            selectedProject.setExpectedOutputFile(expectedPath);
            selectedProject.setConfigurationId(configId);

            projectService.updateProject(selectedProject);
            loadProjects();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Updated");
            alert.setHeaderText(null);
            alert.setContentText("Project updated successfully.");
            alert.showAndWait();
        });

        deleteBtn.setOnAction(e -> {
            if (selectedProject == null) {
                showWarning("No Selection", "Please select a project to delete.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Project");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete the selected project?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                projectService.deleteProject(selectedProject.getId());
                selectedProject = null;

                listView.getSelectionModel().clearSelection();
                nameField.clear();
                zipField.clear();
                expectedField.clear();
                configCombo.getSelectionModel().clearSelection();

                loadProjects();

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Deleted");
                done.setHeaderText(null);
                done.setContentText("Project deleted successfully.");
                done.showAndWait();
            }
        });

        VBox topBox = new VBox(10, title);
        topBox.setPadding(new Insets(0, 0, 10, 0));

        root.setTop(topBox);
        root.setLeft(form);
        root.setCenter(listView);

        Scene scene = new Scene(root, 980, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void loadProjects() {
        List<Project> projects = projectService.getAllProjects();

        if (projects.isEmpty()) {
            listView.setItems(FXCollections.observableArrayList("No saved projects found."));
        } else {
            listView.setItems(FXCollections.observableArrayList(
                    projects.stream()
                            .map(p -> "ID: " + p.getId()
                                    + " | Name: " + p.getName()
                                    + " | ZIP Folder: " + p.getZipFolder()
                                    + " | Expected: " + p.getExpectedOutputFile()
                                    + " | Config ID: " + p.getConfigurationId())
                            .toList()
            ));
        }
    }

    private void loadConfigurations(ComboBox<String> configCombo) {
        configCombo.getItems().clear();

        List<Configuration> configurations = configurationService.getAllConfigurations();
        for (Configuration c : configurations) {
            configCombo.getItems().add(c.getId() + " - " + c.getName());
        }
    }

    private String getConfigurationName(int configId) {
        Configuration configuration = configurationService.getById(configId);
        return configuration != null ? configuration.getName() : "Unknown";
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}