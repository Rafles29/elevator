package gui;

import controler.RealTimeController;
import controllers.Controller;
import elevator.Elevator;
import entity.Clock;
import java.util.Arrays;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final int WINDOW_WIDTH = 1024;
    private final int WINDOW_HEIGHT = 768;
    private final int FLOOR_COUNT = 6;
    private final int ELEVATOR_SIZE = 2;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_window.fxml"));
        Parent root = (Parent)loader.load();
        
        Controller controller = loader.<Controller>getController();
        controller.setAlgorithms(Arrays.asList("Goliat", "Artur", "Rafał", "Karol", "Bmb"));
        controller.setGenerators(Arrays.asList("Mordor Rano", "Mordor Po Południu", "Random"));

        RealTimeController rtc = new RealTimeController(FLOOR_COUNT, ELEVATOR_SIZE, controller);
        controller.setRTC(rtc);
        
        primaryStage.setTitle("Elevator Simulator");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
