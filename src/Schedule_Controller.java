import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Schedule_Controller {

    @FXML TextArea result_box;
    @FXML TextField search_text_box;
    @FXML private Button Back;
    @FXML private Button search_button;

    @FXML
    public void initialize() {
        loadSchedule();
    }

    private void loadSchedule() {        
        File file = new File("schedule.txt");
        if (!file.exists()) {
            String msg = "No schedule file found.";
            search_text_box.setText(msg);
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            content = content.toUpperCase();

            String[] sections = content.split("(?=MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)");

            for (String section : sections) {
                section = section.trim();

                if (section.startsWith("MONDAY")) {
                    monday.setText(clean(section, "MONDAY"));
                } else if (section.startsWith("TUESDAY")) {
                    tuesday.setText(clean(section, "TUESDAY"));
                } else if (section.startsWith("WEDNESDAY")) {
                    wednesday.setText(clean(section, "WEDNESDAY"));
                } else if (section.startsWith("THURSDAY")) {
                    thursday.setText(clean(section, "THURSDAY"));
                } else if (section.startsWith("FRIDAY")) {
                    friday.setText(clean(section, "FRIDAY"));
                } else if (section.startsWith("SATURDAY")) {
                    saturday.setText(clean(section, "SATURDAY"));
                } else if (section.startsWith("SUNDAY")) {
                    sunday.setText(clean(section, "SUNDAY"));
                }
            }

        } catch (IOException e) {
            search.setText("ERROR: Could not read the file!");
        }
    }
    
    private String clean(String section, String day) {
        return day + "\n" + section.replaceFirst(day, "").trim();
    }
    @FXML
    private void searchSchedule() {
        String query = search_text_box.getText();

        if (query == null || query.trim().isEmpty()) {
            loadSchedule();
            return;
        }

        File file = new File("schedule.txt");

        if (!file.exists()) {
            search.setText("No schedule file found.");
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            String[] lines = content.split("\n");
            StringBuilder results = new StringBuilder();

            for (String line : lines) {
                if (line.toLowerCase().contains(query.toLowerCase())) {
                    results.append(line).append("\n");
                }
            }

            if (results.length() == 0) {
                search.setText("No results found.");
            } else {
                search.setText(results.toString());

                monday.clear();
                tuesday.clear();
                wednesday.clear();
                thursday.clear();
                friday.clear();
                saturday.clear();
                sunday.clear();
            }

    } catch (IOException e) {
        search.setText("ERROR reading file.");
        e.printStackTrace();
    }
}
    
    
    @FXML
    void open_welcome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setTitle("Volunteer Tracker");
        stage.setScene(scene);
        stage.show();
    }
}
