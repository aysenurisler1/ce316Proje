package com.kaanege.iae.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpView {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("User Manual");

        VBox content = new VBox(16);
        content.setPadding(new Insets(24));

        Label title = new Label("Integrated Assignment Environment (IAE) - User Manual");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label intro = new Label(
                "IAE is a JavaFX desktop application developed for CE316.\n\n" +
                        "This application helps lecturers manage, process, evaluate and report programming assignments.\n" +
                        "It supports project creation, configuration management, ZIP submission processing,\n" +
                        "automatic source detection, compilation, execution, output comparison and result reporting."
        );
        intro.setWrapText(true);
        intro.setStyle("-fx-font-size: 14px;");

        Label features = new Label(
                "Main Features\n" +
                        "• Configuration Management\n" +
                        "• Project Creation\n" +
                        "• ZIP Submission Processing\n" +
                        "• Automatic Source Detection\n" +
                        "• Compile / Run / Compare\n" +
                        "• Score Calculation\n" +
                        "• Results Management\n" +
                        "• CSV Export\n" +
                        "• JSON Configuration Import / Export"
        );
        features.setWrapText(true);
        features.setStyle("-fx-font-size: 14px;");

        Label step1 = new Label(
                "1. Create a Configuration\n" +
                        "- Open the Configurations screen.\n" +
                        "- Enter Name, Language, Source Pattern, Compile Command, and Run Command.\n" +
                        "- Save the configuration.\n\n" +
                        "Example C Configuration:\n" +
                        "  Name: C GCC\n" +
                        "  Language: C\n" +
                        "  Source Pattern: *.c\n" +
                        "  Compile Command: gcc {source} -o program.exe\n" +
                        "  Run Command: program.exe\n\n" +
                        "Example C++ Configuration:\n" +
                        "  Source Pattern: *.cpp\n" +
                        "  Compile Command: g++ {source} -o program.exe\n" +
                        "  Run Command: program.exe\n\n" +
                        "Example Java Configuration:\n" +
                        "  Source Pattern: Main.java\n" +
                        "  Compile Command: javac {source}\n" +
                        "  Run Command: java Main\n\n" +
                        "Example Python Configuration:\n" +
                        "  Source Pattern: *.py\n" +
                        "  Compile Command: python -m py_compile {source}\n" +
                        "  Run Command: python {source}"
        );
        step1.setWrapText(true);
        step1.setStyle("-fx-font-size: 14px;");

        Label step2 = new Label(
                "2. Create a Project\n" +
                        "- Open the Project Management screen.\n" +
                        "- Enter the project name.\n" +
                        "- Select the ZIP folder or extracted submissions folder.\n" +
                        "- Select the expected output file.\n" +
                        "- Choose the configuration.\n" +
                        "- Click Create Project."
        );
        step2.setWrapText(true);
        step2.setStyle("-fx-font-size: 14px;");

        Label step3 = new Label(
                "3. Process Submissions\n" +
                        "- Use Scan ZIP Folder to list ZIP files and folders.\n" +
                        "- Use Extract All ZIPs to extract submissions.\n" +
                        "- Use Analyze Submissions to detect source files according to Source Pattern.\n" +
                        "- Use Compile Submissions to compile source code using the selected configuration.\n" +
                        "- Use Run and Compare to execute compiled programs and compare outputs."
        );
        step3.setWrapText(true);
        step3.setStyle("-fx-font-size: 14px;");

        Label step4 = new Label(
                "4. Score System\n" +
                        "- Compile Success = 30 points\n" +
                        "- Run Success = 20 points\n" +
                        "- Output Match = 50 points\n\n" +
                        "Possible results:\n" +
                        "- PASS = 100\n" +
                        "- FAIL (wrong output) = 50\n" +
                        "- RUN ERROR = 30\n" +
                        "- COMPILE FAIL = 0"
        );
        step4.setWrapText(true);
        step4.setStyle("-fx-font-size: 14px;");

        Label step5 = new Label(
                "5. View Results\n" +
                        "- Open the Results screen.\n" +
                        "- Select a project.\n" +
                        "- View student result records.\n" +
                        "- View PASS / FAIL / RUN ERROR / COMPILE FAIL status.\n" +
                        "- View score and output message.\n" +
                        "- Export results as CSV if needed."
        );
        step5.setWrapText(true);
        step5.setStyle("-fx-font-size: 14px;");

        Label step6 = new Label(
                "6. Configuration Import / Export\n" +
                        "- Configurations can be exported as JSON files.\n" +
                        "- Configurations can be imported back from JSON files.\n" +
                        "- This makes grading templates reusable for different assignments."
        );
        step6.setWrapText(true);
        step6.setStyle("-fx-font-size: 14px;");

        Label notes = new Label(
                "Important Notes\n" +
                        "- Source Pattern is used to detect the correct source file.\n" +
                        "- The system may use placeholders such as {source}, {sourceName}, and {sourcePath}.\n" +
                        "- Compiler/interpreter tools must be installed and accessible from the system PATH.\n" +
                        "- Commands must be valid for the target operating system.\n" +
                        "- The expected output file must contain the correct output text.\n" +
                        "- Results are stored in SQLite and can be viewed later."
        );
        notes.setWrapText(true);
        notes.setStyle("-fx-font-size: 14px;");

        Label ending = new Label(
                "This software is an integrated assignment environment developed for CE316."
        );
        ending.setWrapText(true);
        ending.setStyle("-fx-font-size: 13px; -fx-font-style: italic;");

        content.getChildren().addAll(
                title, intro, features, step1, step2, step3, step4, step5, step6, notes, ending
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        Scene scene = new Scene(scrollPane, 900, 650);

        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception ignored) {
        }

        stage.setScene(scene);
        stage.show();
    }
}