package game4j;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("game4j.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 400, 300));*/

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        

        FlowPane root = new FlowPane();
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().remove(btn);
                System.out.println("Hello World!");
            }
        });

        Label title = new Label();
        title.setText("Bienvenue sur notre application Game4j");
        root.getChildren().add(title);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Home - Game4j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}