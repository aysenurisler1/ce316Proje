package com.kaanege.iae.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {

    private final BorderPane root;

    public MainView() {
        root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(createMenuBar());
        root.setLeft(createSidebar());
        root.setCenter(createDashboard());
    }

    public Parent getRoot() {
        return root;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newProject = new MenuItem("New Project");
        MenuItem openProject = new MenuItem("Open Project");
        MenuItem saveProject = new MenuItem("Save Project");
        MenuItem exit = new MenuItem("Exit");

        newProject.setOnAction(e -> new ProjectView().show());
        openProject.setOnAction(e -> new ProjectSelectionView().show());

        saveProject.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Project");
            alert.setHeaderText(null);
            alert.setContentText("Projects are already stored in the database when created or updated.");
            alert.showAndWait();
        });

        exit.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(
                newProject,
                openProject,
                saveProject,
                new SeparatorMenuItem(),
                exit
        );

        Menu configurationMenu = new Menu("Configuration");
        MenuItem manageConfigurations = new MenuItem("Manage Configurations");
        MenuItem importConfiguration = new MenuItem("Import Configuration");
        MenuItem exportConfiguration = new MenuItem("Export Configuration");

        manageConfigurations.setOnAction(e -> new ConfigurationView().show());

        importConfiguration.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Import Configuration");
            alert.setHeaderText(null);
            alert.setContentText("Use the Configuration screen to import configurations.");
            alert.showAndWait();
            new ConfigurationView().show();
        });

        exportConfiguration.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Configuration");
            alert.setHeaderText(null);
            alert.setContentText("Use the Configuration screen to export configurations.");
            alert.showAndWait();
            new ConfigurationView().show();
        });

        configurationMenu.getItems().addAll(
                manageConfigurations,
                importConfiguration,
                exportConfiguration
        );

        Menu runMenu = new Menu("Run");
        MenuItem runProject = new MenuItem("Run Current Project");
        runProject.setOnAction(e -> new ProjectView().show());
        runMenu.getItems().add(runProject);

        Menu helpMenu = new Menu("Help");
        MenuItem userManual = new MenuItem("User Manual");
        MenuItem about = new MenuItem("About");

        userManual.setOnAction(e -> new HelpView().show());
        about.setOnAction(e -> showAbout());

        helpMenu.getItems().addAll(userManual, about);

        menuBar.getMenus().addAll(fileMenu, configurationMenu, runMenu, helpMenu);
        return menuBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(14);
        sidebar.setPadding(new Insets(18));
        sidebar.setPrefWidth(240);
        sidebar.getStyleClass().add("sidebar");

        Label logo = new Label("IAE");
        logo.getStyleClass().add("title-label");

        Label subtitle = new Label("Integrated Assignment Environment");
        subtitle.setWrapText(true);
        subtitle.getStyleClass().add("subtitle-label");

        Separator separator = new Separator();

        Button dashboardBtn = createNavButton("Dashboard");
        Button newProjectBtn = createNavButton("New Project");
        Button openProjectsBtn = createNavButton("Open Projects");
        Button configBtn = createNavButton("Configurations");
        Button runBtn = createNavButton("Run Project");
        Button resultsBtn = createNavButton("Results");
        Button aboutBtn = createNavButton("About");

        dashboardBtn.setOnAction(e -> root.setCenter(createDashboard()));
        newProjectBtn.setOnAction(e -> new ProjectView().show());
        openProjectsBtn.setOnAction(e -> new ProjectSelectionView().show());
        configBtn.setOnAction(e -> new ConfigurationView().show());
        runBtn.setOnAction(e -> new ProjectView().show());
        resultsBtn.setOnAction(e -> new ResultsView().show());
        aboutBtn.setOnAction(e -> showAbout());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("Developed by S1T3");
        footer.getStyleClass().add("sidebar-footer");

        sidebar.getChildren().addAll(
                logo,
                subtitle,
                separator,
                dashboardBtn,
                newProjectBtn,
                openProjectsBtn,
                configBtn,
                runBtn,
                resultsBtn,
                aboutBtn,
                spacer,
                footer
        );

        return sidebar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(44);
        button.getStyleClass().add("modern-nav-button");
        return button;
    }

    private VBox createDashboard() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(24));

        VBox headerCard = createCard();
        Label title = new Label("Integrated Assignment Environment");
        title.getStyleClass().add("title-label");

        Label subtitle = new Label("CE316 Project - Programming Assignment Management System");
        subtitle.getStyleClass().add("subtitle-label");

        Label intro = new Label(
                "Manage projects, configurations, submissions and grading workflows from one place."
        );
        intro.setWrapText(true);

        headerCard.getChildren().addAll(title, subtitle, intro);

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
                createStatCard("Projects", "Manage saved project records"),
                createStatCard("Configurations", "Reusable grading templates"),
                createStatCard("Submissions", "ZIP processing and analysis"),
                createStatCard("Results", "Scores, status and exports")
        );

        HBox bottomRow = new HBox(16);

        VBox quickActions = createCard();
        quickActions.setPrefWidth(420);

        Label quickTitle = new Label("Quick Actions");
        quickTitle.getStyleClass().add("section-label");

        Button createProjectBtn = new Button("Create New Project");
        Button manageConfigBtn = new Button("Open Configurations");
        Button openResultsBtn = new Button("Open Results");

        createProjectBtn.setMaxWidth(Double.MAX_VALUE);
        manageConfigBtn.setMaxWidth(Double.MAX_VALUE);
        openResultsBtn.setMaxWidth(Double.MAX_VALUE);

        createProjectBtn.setPrefHeight(42);
        manageConfigBtn.setPrefHeight(42);
        openResultsBtn.setPrefHeight(42);

        createProjectBtn.getStyleClass().add("primary-button");
        manageConfigBtn.getStyleClass().add("primary-button");
        openResultsBtn.getStyleClass().add("primary-button");

        createProjectBtn.setOnAction(e -> new ProjectView().show());
        manageConfigBtn.setOnAction(e -> new ConfigurationView().show());
        openResultsBtn.setOnAction(e -> new ResultsView().show());

        quickActions.getChildren().addAll(
                quickTitle,
                createProjectBtn,
                manageConfigBtn,
                openResultsBtn
        );

        VBox overview = createCard();
        HBox.setHgrow(overview, Priority.ALWAYS);

        Label overviewTitle = new Label("System Overview");
        overviewTitle.getStyleClass().add("section-label");

        Label overviewText = new Label(
                "Main capabilities:\n\n" +
                        "• Create and manage projects\n" +
                        "• Create, update, import and export configurations\n" +
                        "• Process student ZIP submissions in bulk\n" +
                        "• Detect source files using Source Pattern\n" +
                        "• Compile and run student code automatically\n" +
                        "• Compare actual output with expected output\n" +
                        "• Save grading results in SQLite\n" +
                        "• Export results as CSV"
        );
        overviewText.setWrapText(true);

        overview.getChildren().addAll(overviewTitle, overviewText);

        bottomRow.getChildren().addAll(quickActions, overview);

        container.getChildren().addAll(headerCard, statsRow, bottomRow);
        return container;
    }

    private VBox createCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        return card;
    }

    private VBox createStatCard(String title, String description) {
        VBox card = createCard();
        card.setPrefWidth(220);
        card.getStyleClass().add("stat-card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.getStyleClass().add("stat-desc");

        card.getChildren().addAll(titleLabel, descLabel);
        return card;
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Integrated Assignment Environment");
        alert.setContentText(
                "Izmir University of Economics\n" +
                        "CE316 Project\n" +
                        "Version: 1.0\n\n" +
                        "Integrated Assignment Environment for programming assignment management.\n\n" +
                        "Developers:\n" +
                        "Kaan Ege SOYLU\n" +
                        "Feyza GEREME\n" +
                        "Nehir KARAMARTİNLER\n" +
                        "Mert BARMANBEK\n" +
                        "Ayşenur İŞLER\n" +
                        "Arjin ÖZCEYLAN\n\n" +
                        "© 2026 S1T3 Developers"
        );
        alert.showAndWait();
    }
}