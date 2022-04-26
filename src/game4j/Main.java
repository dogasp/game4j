package game4j;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static int windowWidth = 800;
    public static int WindowHeight = 600;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Interface de bienvenue, on peut afficher les rêgles du jeu / lancer une partie*/

        Button btnStart = new Button();
        btnStart.setText("Lancer Jeu");
        AnchorPane.setTopAnchor(btnStart, 200.0);
        AnchorPane.setLeftAnchor(btnStart, 280.0);
        

        AnchorPane root = new AnchorPane();
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Lancement du jeu ...");
                root.getChildren().clear();
                Game game = new Game(root);
                game.loadMap("ressources/maps/test.map");;
            }
        });

        Button btnLoad = new Button();
        btnLoad.setText("Charger partie");
        AnchorPane.setTopAnchor(btnLoad, 300.0);
        AnchorPane.setLeftAnchor(btnLoad, 280.0);

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //on regarde s'il y a une sauvegarde
                File file = new File("ressources/saves/sauvegarde.dat");
                if (file.exists()){
                    Game game = new Game(root);
                    game.loadGame();
                    //file.delete();
                }
            }
        });

        Label title = new Label();
        title.setText("Bienvenue sur notre application Game4j");
        AnchorPane.setTopAnchor(title, 150.0);
        AnchorPane.setLeftAnchor(title, 180.0);


        root.getChildren().addAll(btnStart, title, btnLoad);

        Scene scene = new Scene(root, windowWidth, WindowHeight);

        primaryStage.setTitle("Home - Game4j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}