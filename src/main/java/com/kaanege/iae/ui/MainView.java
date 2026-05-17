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
}