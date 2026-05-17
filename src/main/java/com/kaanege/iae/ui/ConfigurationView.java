package com.kaanege.iae.ui;

import com.kaanege.iae.model.Configuration;
import com.kaanege.iae.service.ConfigurationFileService;
import com.kaanege.iae.service.ConfigurationService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ConfigurationView {

    private final ConfigurationService configurationService = new ConfigurationService();
    private final ConfigurationFileService fileService = new ConfigurationFileService();

    private final ListView<Configuration> listView = new ListView<>();
    private Configuration selected;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Manage Configurations");

        VBox page = new VBox(18);
        page.setPadding(new Insets(20));
        page.getStyleClass().add("app-root");

        VBox header = createCard();

        Label title = new Label("Configuration Management");
        title.getStyleClass().add("title-label");

        Label sub = new Label(
                "Create reusable grading templates, manage configurations, import/export JSON files and use language presets."
        );
        sub.getStyleClass().add("subtitle-label");
        sub.setWrapText(true);

        header.getChildren().addAll(title, sub);

        TextField nameField = new TextField();
        TextField languageField = new TextField();
        TextField patternField = new TextField();
        TextField compileField = new TextField();
        TextField runField = new TextField();

        nameField.setPromptText("Example: C GCC");
        languageField.setPromptText("Example: C");
        patternField.setPromptText("Example: *.c");
        compileField.setPromptText("Example: gcc {sourceName} -o program.exe");
        runField.setPromptText("Example: program.exe");

        VBox editorCard = createCard();
        editorCard.setPrefWidth(760);
        HBox.setHgrow(editorCard, Priority.ALWAYS);

        Label editorTitle = new Label("Configuration Editor");
        editorTitle.getStyleClass().add("section-label");

        Label editorInfo = new Label(
                "Fill the fields manually or use a quick template, then save the configuration."
        );
        editorInfo.getStyleClass().add("subtitle-label");
        editorInfo.setWrapText(true);

        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(14);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPrefWidth(150);

        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setHgrow(Priority.ALWAYS);

        form.getColumnConstraints().addAll(leftCol, rightCol);

        form.add(new Label("Name"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(new Label("Language"), 0, 1);
        form.add(languageField, 1, 1);

        form.add(new Label("Source Pattern"), 0, 2);
        form.add(patternField, 1, 2);

        form.add(new Label("Compile Command"), 0, 3);
        form.add(compileField, 1, 3);

        form.add(new Label("Run Command"), 0, 4);
        form.add(runField, 1, 4);

        Button saveBtn = new Button("Save");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");
        Button importBtn = new Button("Import");
        Button exportBtn = new Button("Export");
        Button refreshBtn = new Button("Refresh");

        saveBtn.getStyleClass().add("primary-button");
        updateBtn.getStyleClass().add("primary-button");

        saveBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        importBtn.setMaxWidth(Double.MAX_VALUE);
        exportBtn.setMaxWidth(Double.MAX_VALUE);
        refreshBtn.setMaxWidth(Double.MAX_VALUE);

        HBox primaryRow = new HBox(10, saveBtn, updateBtn);
        HBox secondaryRow = new HBox(10, deleteBtn, clearBtn);
        HBox fileRow = new HBox(10, importBtn, exportBtn, refreshBtn);

        HBox.setHgrow(saveBtn, Priority.ALWAYS);
        HBox.setHgrow(updateBtn, Priority.ALWAYS);
        HBox.setHgrow(deleteBtn, Priority.ALWAYS);
        HBox.setHgrow(clearBtn, Priority.ALWAYS);
        HBox.setHgrow(importBtn, Priority.ALWAYS);
        HBox.setHgrow(exportBtn, Priority.ALWAYS);
        HBox.setHgrow(refreshBtn, Priority.ALWAYS);

        VBox actionBox = new VBox(10, primaryRow, secondaryRow, fileRow);

        editorCard.getChildren().addAll(editorTitle, editorInfo, form, actionBox);

        VBox templateCard = createCard();
        templateCard.setPrefWidth(420);

        Label templateTitle = new Label("Quick Templates");
        templateTitle.getStyleClass().add("section-label");

        Label templateInfo = new Label(
                "Click any language to auto-fill the form with a common configuration template."
        );
        templateInfo.getStyleClass().add("subtitle-label");
        templateInfo.setWrapText(true);

        GridPane templateGrid = new GridPane();
        templateGrid.setHgap(10);
        templateGrid.setVgap(10);

        ColumnConstraints tc1 = new ColumnConstraints();
        ColumnConstraints tc2 = new ColumnConstraints();
        tc1.setHgrow(Priority.ALWAYS);
        tc2.setHgrow(Priority.ALWAYS);
        templateGrid.getColumnConstraints().addAll(tc1, tc2);

        String[] languages = {
                "C", "C++",
                "Java", "Python",
                "C#", "JavaScript",
                "Go", "Rust"
        };

        int index = 0;
        for (String lang : languages) {
            Button btn = new Button(lang);
            btn.getStyleClass().add("workflow-button");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(42);

            int col = index % 2;
            int row = index / 2;

            templateGrid.add(btn, col, row);

            btn.setOnAction(e -> applyPreset(
                    lang,
                    nameField,
                    languageField,
                    patternField,
                    compileField,
                    runField
            ));

            index++;
        }

        Label notesTitle = new Label("Template Notes");
        notesTitle.getStyleClass().add("section-label");

        Label notes = new Label(
                "• Use {sourceName} for source file name.\n" +
                        "• Use {mainClass} for Java / executable class names.\n" +
                        "• Python and JavaScript usually do not require a compile step.\n" +
                        "• You can modify preset values before saving.\n" +
                        "• Source Pattern is used to detect the correct file automatically."
        );
        notes.setWrapText(true);

        templateCard.getChildren().addAll(templateTitle, templateInfo, templateGrid, notesTitle, notes);

        HBox topRow = new HBox(18, editorCard, templateCard);
        HBox.setHgrow(editorCard, Priority.ALWAYS);
        HBox.setHgrow(templateCard, Priority.ALWAYS);

        VBox savedCard = createCard();
        VBox.setVgrow(savedCard, Priority.ALWAYS);

        Label listTitle = new Label("Saved Configurations");
        listTitle.getStyleClass().add("section-label");

        Label listInfo = new Label(
                "Search and select a saved configuration to edit, delete or export it."
        );
        listInfo.getStyleClass().add("subtitle-label");
        listInfo.setWrapText(true);

        TextField searchField = new TextField();
        searchField.setPromptText("Search configurations by name, language or source pattern...");

        listView.setPrefHeight(340);
        VBox.setVgrow(listView, Priority.ALWAYS);

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Configuration c, boolean empty) {
                super.updateItem(c, empty);

                if (empty || c == null) {
                    setText(null);
                    return;
                }

                String pattern = c.getSourcePattern() == null ? "" : c.getSourcePattern();
                setText(
                        c.getId() + " | " +
                                c.getName() + " | " +
                                c.getLanguage() + " | " +
                                pattern
                );
            }
        });

        savedCard.getChildren().addAll(listTitle, listInfo, searchField, listView);

        page.getChildren().addAll(header, topRow, savedCard);

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);

        refreshBtn.setOnAction(e -> load());

        clearBtn.setOnAction(e -> {
            selected = null;
            listView.getSelectionModel().clearSelection();
            clearFields(nameField, languageField, patternField, compileField, runField);
        });

        saveBtn.setOnAction(e -> {
            if (!validateFields(nameField, languageField, patternField, runField)) {
                return;
            }

            Configuration config = new Configuration(
                    nameField.getText().trim(),
                    languageField.getText().trim(),
                    compileField.getText().trim(),
                    runField.getText().trim(),
                    patternField.getText().trim()
            );

            configurationService.saveConfiguration(config);
            load();
            clearFields(nameField, languageField, patternField, compileField, runField);
            showInfo("Saved", "Configuration saved successfully.");
        });

        updateBtn.setOnAction(e -> {
            if (selected == null) {
                showWarning("No Selection", "Please select a configuration to update.");
                return;
            }

            if (!validateFields(nameField, languageField, patternField, runField)) {
                return;
            }

            selected.setName(nameField.getText().trim());
            selected.setLanguage(languageField.getText().trim());
            selected.setSourcePattern(patternField.getText().trim());
            selected.setCompileCommand(compileField.getText().trim());
            selected.setRunCommand(runField.getText().trim());

            configurationService.updateConfiguration(selected);
            load();
            showInfo("Updated", "Configuration updated successfully.");
        });

        deleteBtn.setOnAction(e -> {
            if (selected == null) {
                showWarning("No Selection", "Please select a configuration to delete.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Configuration");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete the selected configuration?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                configurationService.deleteConfiguration(selected.getId());
                selected = null;
                load();
                clearFields(nameField, languageField, patternField, compileField, runField);
                listView.getSelectionModel().clearSelection();
                showInfo("Deleted", "Configuration deleted successfully.");
            }
        });

        importBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Configuration");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                Configuration config = fileService.importConfiguration(file);

                if (config != null) {
                    configurationService.saveConfiguration(config);
                    load();
                    showInfo("Import Successful", "Configuration imported successfully.");
                } else {
                    showError("Import Failed", "Could not read the configuration file.");
                }
            }
        });

        exportBtn.setOnAction(e -> {
            if (selected == null) {
                showWarning("No Selection", "Please select a configuration to export.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Configuration");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );
            fileChooser.setInitialFileName(selected.getName().replace(" ", "_") + ".json");

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                boolean success = fileService.exportConfiguration(file, selected);

                if (success) {
                    showInfo("Export Successful", "Configuration exported successfully.");
                } else {
                    showError("Export Failed", "Configuration could not be exported.");
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            selected = newValue;

            nameField.setText(newValue.getName());
            languageField.setText(newValue.getLanguage());
            patternField.setText(newValue.getSourcePattern());
            compileField.setText(newValue.getCompileCommand());
            runField.setText(newValue.getRunCommand());
        });

        searchField.textProperty().addListener((obs, oldValue, newValue) -> filter(newValue));

        load();

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(1180, bounds.getWidth() - 100);
        double height = Math.min(760, bounds.getHeight() - 100);

        Scene scene = new Scene(scroll, width, height);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(680);
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();
    }

    private VBox createCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card");
        return card;
    }

    private boolean validateFields(
            TextField nameField,
            TextField languageField,
            TextField patternField,
            TextField runField
    ) {
        if (nameField.getText().trim().isEmpty()) {
            showWarning("Missing Data", "Please enter a configuration name.");
            return false;
        }

        if (languageField.getText().trim().isEmpty()) {
            showWarning("Missing Data", "Please enter a language.");
            return false;
        }

        if (patternField.getText().trim().isEmpty()) {
            showWarning("Missing Data", "Please enter a source pattern.");
            return false;
        }

        if (runField.getText().trim().isEmpty()) {
            showWarning("Missing Data", "Please enter a run command.");
            return false;
        }

        return true;
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void load() {
        List<Configuration> configurations = configurationService.getAllConfigurations();
        listView.setItems(FXCollections.observableArrayList(configurations));
    }

    private void filter(String text) {
        String query = text == null ? "" : text.trim().toLowerCase();

        listView.setItems(FXCollections.observableArrayList(
                configurationService.getAllConfigurations()
                        .stream()
                        .filter(c ->
                                safe(c.getName()).contains(query) ||
                                        safe(c.getLanguage()).contains(query) ||
                                        safe(c.getSourcePattern()).contains(query))
                        .toList()
        ));
    }

    private String safe(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private void applyPreset(
            String lang,
            TextField name,
            TextField language,
            TextField pattern,
            TextField compile,
            TextField run
    ) {
        switch (lang) {
            case "C" -> {
                name.setText("C GCC");
                language.setText("C");
                pattern.setText("*.c");
                compile.setText("gcc {sourceName} -o program.exe");
                run.setText("program.exe");
            }

            case "C++" -> {
                name.setText("C++ G++");
                language.setText("C++");
                pattern.setText("*.cpp");
                compile.setText("g++ {sourceName} -o program.exe");
                run.setText("program.exe");
            }

            case "Java" -> {
                name.setText("Java JDK");
                language.setText("Java");
                pattern.setText("*.java");
                compile.setText("javac {sourceName}");
                run.setText("java {mainClass}");
            }

            case "Python" -> {
                name.setText("Python 3");
                language.setText("Python");
                pattern.setText("*.py");
                compile.setText("");
                run.setText("python {sourceName}");
            }

            case "C#" -> {
                name.setText("C#");
                language.setText("C#");
                pattern.setText("*.cs");
                compile.setText("csc {sourceName}");
                run.setText("{mainClass}.exe");
            }

            case "JavaScript" -> {
                name.setText("NodeJS");
                language.setText("JavaScript");
                pattern.setText("*.js");
                compile.setText("");
                run.setText("node {sourceName}");
            }

            case "Go" -> {
                name.setText("Go");
                language.setText("Go");
                pattern.setText("*.go");
                compile.setText("go build -o program.exe {sourceName}");
                run.setText("program.exe");
            }

            case "Rust" -> {
                name.setText("Rust");
                language.setText("Rust");
                pattern.setText("*.rs");
                compile.setText("rustc {sourceName}");
                run.setText("{mainClass}.exe");
            }
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
}