import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class Hour_Tracker_Controller {
    
    String Name, New_Note;
    int Hours, Minutes; 
    
    @FXML
    private Spinner<Integer> Hour;
    
    @FXML
    private Spinner<Integer> Minute;

    @FXML
    private TextField Volunteer_Name;
    
    @FXML
    private TextField Heads_Up;
    
    @FXML
    private TextField Volunteer_Notes;
   
    @FXML
    private ListView<String> Record_List;

    @FXML
    private Button Log_Button;
    
    @FXML
    private Button Search_Button;
    
    @FXML
    private Button Back;
    
    @FXML
    private Button Delete_Button;
    
    
    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> svfh =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        svfh.setValue(0);
        Hour.setValueFactory(svfh);

        SpinnerValueFactory<Integer> svfm =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        svfm.setValue(0);
        Minute.setValueFactory(svfm);

        Heads_Up.setText("Enter a name to search or log hours!");
        loadEntries();
    }
    
    @FXML
    private void Log() {
        Name = Volunteer_Name.getText();
        Hours = Hour.getValue();
        Minutes = Minute.getValue();
        New_Note = Volunteer_Notes.getText();

        if (Minutes == 0 && Hours == 0) {
            Heads_Up.setText("You nust enter at least one minute to log time!");
            return;}

        if (Name == null || Name.trim().isEmpty()){
             Heads_Up.setText("You nuust enter a name to log hours!");
            return;
        }

        String entry =
            "-----NEW ENTRY-----\n" +
            "Name: " + Name + "\n" +
            "Hours: " + Hours + "\n" +
            "Minutes: " + Minutes + "\n" +
            "Notes: " + New_Note + "\n" +
            "-----END ENTRY-----\n\n";

        File file = new File("log.txt");

        try {
            Files.writeString(
                file.toPath(),
                entry,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );

            loadEntries();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Volunteer_Notes.clear();
        Hour.getValueFactory().setValue(0);
        Minute.getValueFactory().setValue(0);
        Heads_Up.setText("Added to the file!");
    }
    
    @FXML
    private void Search() {
        Name = Volunteer_Name.getText();

        if (Name == null || Name.trim().isEmpty()) {
            loadEntries();
            return;
        }

        File file = new File("log.txt");
        ObservableList<String> results = FXCollections.observableArrayList();

        int totalHours = 0;
        int totalMinutes = 0;

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] entries = content.split("-----END ENTRY-----");

            for (String entry : entries) {
                if (entry.toLowerCase().contains("name: " + Name.toLowerCase())) {
                    results.add(entry.trim());

                    String[] lines = entry.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("Hours: ")) {
                            totalHours += Integer.parseInt(line.replace("Hours: ", "").trim());
                        }
                        if (line.startsWith("Minutes: ")) {
                            totalMinutes += Integer.parseInt(line.replace("Minutes: ", "").trim());
                        }
                    }
                }
            }

            totalHours += totalMinutes / 60;
            totalMinutes = totalMinutes % 60;

            Record_List.setItems(results);

            Heads_Up.setText(
                "Total: " + totalHours + " hours, " + totalMinutes + " minutes"
            );

        } catch (IOException e) {
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
    
    private void loadEntries() {
        File file = new File("log.txt");

        ObservableList<String> items = FXCollections.observableArrayList();

        if (!file.exists()) {
            Record_List.setItems(items);
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] entries = content.split("-----END ENTRY-----");

            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    items.add(entry.trim());
                }
            }

            Record_List.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void Delete() {
        String selected = Record_List.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        File file = new File("log.txt");

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] entries = content.split("-----END ENTRY-----");

            StringBuilder newContent = new StringBuilder();

            for (String entry : entries) {
                if (!entry.trim().isEmpty() && !entry.trim().equals(selected.trim())) {
                    newContent.append(entry).append("-----END ENTRY-----\n\n");
                }
            }

            Files.writeString(file.toPath(), newContent.toString(), StandardCharsets.UTF_8);

            Heads_Up.setText("Deleted record!");
            loadEntries();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
