import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Welcome_Controller {

    @FXML
    private TextArea admin_message;

    @FXML
    private Button open_hour_tracker;
    
    @FXML
    private Button View_Schedule;

    @FXML
    void open_hour_tracker(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Hour_Tracker.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) open_hour_tracker.getScene().getWindow();

        stage.setTitle("Voulenteer Tracker");
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void open_schedule(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Schedule.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) open_hour_tracker.getScene().getWindow();

        stage.setTitle("Voulenteer Tracker");
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void initialize() {
        File file = new File("message.txt");

        if (!file.exists()) {
            admin_message.setText("No message has been set yet!\n\n To set an admin message edit the text file called \"message.txt\".");
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            admin_message.setText(content);
        } catch (IOException e) {
            admin_message.setText("No message has been set yet!\n\n To set an admin message edit the text file called \"message.txt\".");
            e.printStackTrace();
        }
    }
}