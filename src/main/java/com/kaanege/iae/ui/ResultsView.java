package com.kaanege.iae.ui;

import com.kaanege.iae.model.ExecutionResult;
import com.kaanege.iae.model.Project;
import com.kaanege.iae.service.CsvExportService;
import com.kaanege.iae.service.ProjectService;
import com.kaanege.iae.service.ResultService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class ResultsView {

    private final ResultService resultService = new ResultService();
    private final ProjectService projectService = new ProjectService();
    private final CsvExportService csvExportService = new CsvExportService();

    private final ObservableList<ExecutionResult> allResults = FXCollections.observableArrayList();
    private final ObservableList<ExecutionResult> filteredResults = FXCollections.observableArrayList();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Results");

        VBox page = new VBox(18);
        page.setPadding(new Insets(20));
        page.getStyleClass().add("app-root");

        VBox headerCard = createCard();

        Label title = new Label("Project Results");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label(
                "Review grading results, filter students, analyze scores and export selected project results as CSV."
        );
        subtitle.getStyleClass().add("subtitle-label");
        subtitle.setWrapText(true);

        headerCard.getChildren().addAll(title, subtitle);

        HBox summaryRow = new HBox(16);

        VBox totalCard = createStatCard("Total Results", "0");
        VBox passCard = createStatCard("Passed", "0");
        VBox failCard = createStatCard("Failed / Errors", "0");
        VBox avgCard = createStatCard("Average Score", "0");

        Label totalValue = (Label) totalCard.getChildren().get(1);
        Label passValue = (Label) passCard.getChildren().get(1);
        Label failValue = (Label) failCard.getChildren().get(1);
        Label avgValue = (Label) avgCard.getChildren().get(1);

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(passCard, Priority.ALWAYS);
        HBox.setHgrow(failCard, Priority.ALWAYS);
        HBox.setHgrow(avgCard, Priority.ALWAYS);

        summaryRow.getChildren().addAll(totalCard, passCard, failCard, avgCard);

        VBox filterCard = createCard();

        Label filterTitle = new Label("Filters and Actions");
        filterTitle.getStyleClass().add("section-label");

        ComboBox<Project> projectComboBox = new ComboBox<>();
        projectComboBox.setPromptText("Select project");
        projectComboBox.setPrefWidth(320);

        projectComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getName());
                }
            }
        });

        projectComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select project");
                } else {
                    setText(item.getId() + " - " + item.getName());
                }
            }
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Search by student ID...");
        searchField.setPrefWidth(220);

        ComboBox<String> statusFilterBox = new ComboBox<>();
        statusFilterBox.setPrefWidth(180);
        statusFilterBox.setItems(FXCollections.observableArrayList(
                "All Statuses",
                "PASS",
                "FAIL",
                "RUN ERROR",
                "COMPILE FAIL"
        ));
        statusFilterBox.getSelectionModel().selectFirst();

        Button refreshButton = new Button("Refresh");
        Button exportButton = new Button("Export CSV");
        exportButton.getStyleClass().add("primary-button");

        HBox row1 = new HBox(12, projectComboBox, searchField, statusFilterBox, refreshButton, exportButton);
        filterCard.getChildren().addAll(filterTitle, row1);

        VBox tableCard = createCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        Label tableTitle = new Label("Detailed Results");
        tableTitle.getStyleClass().add("section-label");

        Label tableInfo = new Label(
                "Browse individual student results including status, score and output summary."
        );
        tableInfo.getStyleClass().add("subtitle-label");
        tableInfo.setWrapText(true);

        TableView<ExecutionResult> tableView = new TableView<>();
        tableView.setItems(filteredResults);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<ExecutionResult, String> studentColumn = new TableColumn<>("Student ID");
        studentColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudentId()));
        studentColumn.setPrefWidth(170);

        TableColumn<ExecutionResult, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus()));
        statusColumn.setPrefWidth(150);

        TableColumn<ExecutionResult, Number> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getScore()));
        scoreColumn.setPrefWidth(110);

        TableColumn<ExecutionResult, String> outputColumn = new TableColumn<>("Output / Message");
        outputColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMessage()));
        outputColumn.setPrefWidth(560);

        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(item);

                if (item.equalsIgnoreCase("PASS")) {
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else if (item.equalsIgnoreCase("FAIL")) {
                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else if (item.equalsIgnoreCase("RUN ERROR")) {
                    setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                } else if (item.equalsIgnoreCase("COMPILE FAIL")) {
                    setStyle("-fx-text-fill: #b91c1c; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });

        scoreColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                int score = item.intValue();
                setText(String.valueOf(score));

                if (score == 100) {
                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else if (score >= 50) {
                    setStyle("-fx-text-fill: #1d4ed8; -fx-font-weight: bold;");
                } else if (score > 0) {
                    setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-text-fill: #b91c1c; -fx-font-weight: bold;");
                }
            }
        });

        outputColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    return;
                }

                String shortened = item.length() > 120 ? item.substring(0, 117) + "..." : item;
                setText(shortened);

                if (item.length() > 120) {
                    setTooltip(new javafx.scene.control.Tooltip(item));
                } else {
                    setTooltip(null);
                }
            }
        });

        tableView.getColumns().addAll(studentColumn, statusColumn, scoreColumn, outputColumn);
        tableCard.getChildren().addAll(tableTitle, tableInfo, tableView);

        page.getChildren().addAll(headerCard, summaryRow, filterCard, tableCard);

        Runnable loadProjects = () -> {
            List<Project> projects = projectService.getAllProjects();
            projectComboBox.setItems(FXCollections.observableArrayList(projects));

            if (!projects.isEmpty() && projectComboBox.getValue() == null) {
                projectComboBox.getSelectionModel().selectFirst();
            }
        };

        Runnable loadResultsForSelectedProject = () -> {
            Project selectedProject = projectComboBox.getValue();

            if (selectedProject == null) {
                allResults.clear();
                applyFilters(searchField, statusFilterBox, totalValue, passValue, failValue, avgValue);
                return;
            }

            List<ExecutionResult> results = resultService.getResultsByProjectId(selectedProject.getId());
            allResults.setAll(results);
            applyFilters(searchField, statusFilterBox, totalValue, passValue, failValue, avgValue);
        };

        loadProjects.run();
        loadResultsForSelectedProject.run();

        refreshButton.setOnAction(e -> {
            loadProjects.run();
            loadResultsForSelectedProject.run();
        });

        projectComboBox.setOnAction(e -> loadResultsForSelectedProject.run());

        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                applyFilters(searchField, statusFilterBox, totalValue, passValue, failValue, avgValue)
        );

        statusFilterBox.setOnAction(e ->
                applyFilters(searchField, statusFilterBox, totalValue, passValue, failValue, avgValue)
        );

        exportButton.setOnAction(e -> {
            Project selectedProject = projectComboBox.getValue();

            if (selectedProject == null) {
                showWarning("No Project", "Please select a project first.");
                return;
            }

            List<ExecutionResult> results = resultService.getResultsByProjectId(selectedProject.getId());

            if (results.isEmpty()) {
                showWarning("No Results", "There are no results to export for the selected project.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Results CSV");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            fileChooser.setInitialFileName(selectedProject.getName().replace(" ", "_") + "_results.csv");

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                boolean success = csvExportService.exportResults(selectedFile, results);

                if (success) {
                    showInfo("Export Successful", "Results exported successfully.");
                } else {
                    showError("Export Failed", "Failed to export CSV.");
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(1180, bounds.getWidth() - 100);
        double height = Math.min(800, bounds.getHeight() - 100);

        Scene scene = new Scene(scrollPane, width, height);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(680);
        stage.setMaxWidth(bounds.getWidth());
        stage.setMaxHeight(bounds.getHeight());
        stage.centerOnScreen();
        stage.show();
    }

    private VBox createCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        return card;
    }

    private VBox createStatCard(String title, String value) {
        VBox card = createCard();
        card.getStyleClass().add("stat-card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void applyFilters(
            TextField searchField,
            ComboBox<String> statusFilterBox,
            Label totalValue,
            Label passValue,
            Label failValue,
            Label avgValue
    ) {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedStatus = statusFilterBox.getValue();

        List<ExecutionResult> resultList = allResults.stream()
                .filter(r -> {
                    boolean matchesSearch = query.isEmpty() ||
                            safe(r.getStudentId()).contains(query);

                    boolean matchesStatus = selectedStatus == null ||
                            selectedStatus.equals("All Statuses") ||
                            safe(r.getStatus()).equals(selectedStatus.toLowerCase(Locale.ROOT));

                    return matchesSearch && matchesStatus;
                })
                .toList();

        filteredResults.setAll(resultList);
        updateSummary(resultList, totalValue, passValue, failValue, avgValue);
    }

    private void updateSummary(
            List<ExecutionResult> results,
            Label totalValue,
            Label passValue,
            Label failValue,
            Label avgValue
    ) {
        int total = results.size();
        long passCount = results.stream()
                .filter(r -> "PASS".equalsIgnoreCase(r.getStatus()))
                .count();

        long failCount = results.stream()
                .filter(r -> !"PASS".equalsIgnoreCase(r.getStatus()))
                .count();

        double avg = results.stream()
                .mapToInt(ExecutionResult::getScore)
                .average()
                .orElse(0);

        totalValue.setText(String.valueOf(total));
        passValue.setText(String.valueOf(passCount));
        failValue.setText(String.valueOf(failCount));
        avgValue.setText(String.format(Locale.US, "%.1f", avg));
    }

    private String safe(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
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