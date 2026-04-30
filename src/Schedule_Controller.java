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
            result_box.setText("No schedule file found.");
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            result_box.setText(content);

        } catch (IOException e) {
            result_box.setText("ERROR: Could not read the file!");
            e.printStackTrace();
        }
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
            result_box.setText("No schedule file found.");
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            String[] lines = content.split("\n");
            StringBuilder results = new StringBuilder();

        for (String line : lines) {
        String lower = line.toLowerCase().trim();

        if (lower.equals("monday") || lower.equals("tuesday") ||
            lower.equals("wednesday") || lower.equals("thursday") ||
            lower.equals("friday") || lower.equals("saturday") ||
            lower.equals("sunday")) {
            continue;
        }

        if (lower.contains(query.toLowerCase())) {
            results.append(line).append("\n");
        }
    }

            if (results.length() == 0) {
                result_box.setText("No results found.");
            } else {
                result_box.setText(results.toString());
            }

        } catch (IOException e) {
            result_box.setText("ERROR reading file.");
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