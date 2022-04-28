package game4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static int windowWidth = 800;
    public static int WindowHeight = 600;

    private Scene scene;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Interface de bienvenue, on peut afficher les rêgles du jeu / lancer une partie*/
        this.primaryStage = primaryStage;

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
        AnchorPane.setTopAnchor(btnLoad, 300.0);
        AnchorPane.setLeftAnchor(btnLoad, 280.0);

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                Game game = new Game(root);
                game.loadGame("ressources/saves/sauvegarde.dat");
            }
        });

        File file = new File("ressources/saves/sauvegarde.dat");
        if (file.exists()){
            Path path = Paths.get("ressources/saves/sauvegarde.dat");
            try {
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                
                LocalDateTime localDateTime = attr.creationTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
                btnLoad.setText("Charger partie du " + localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            root.getChildren().add(btnLoad);
        }

        Label title = new Label();
        title.setText("Bienvenue sur notre application Game4j");
        AnchorPane.setTopAnchor(title, 150.0);
        AnchorPane.setLeftAnchor(title, 180.0);


        Button btnReplay = new Button();
        btnReplay.setText("Charger un replay");
        AnchorPane.setTopAnchor(btnReplay, 200.0);
        AnchorPane.setRightAnchor(btnReplay, 100.0);

        if (Paths.get("ressources/replay").toFile().listFiles().length > 0){
            root.getChildren().add(btnReplay);
        }

        root.getChildren().addAll(btnStart, title);

        this.scene = new Scene(root, windowWidth, WindowHeight);

        Main main = this; //pour l'envoyer dans le replay

        btnReplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                AnchorPane replayPane = new AnchorPane();

                Label desc = new Label();
                desc.setText("Selectionnez un replay à lire");
                AnchorPane.setTopAnchor(desc, 20.0);
                AnchorPane.setLeftAnchor(desc, 50.0);

                Button cancelBtn = new Button();
                cancelBtn.setText("Cancel");
                AnchorPane.setBottomAnchor(cancelBtn, 20.0);
                AnchorPane.setLeftAnchor(cancelBtn, 50.0);

                cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        primaryStage.setScene(scene);
                    }
                });

                String elt[];

                File dir = new File("ressources/replay");
                elt = dir.list();

                ComboBox<String> listReplay = new ComboBox<String>(FXCollections.observableArrayList(elt));
                AnchorPane.setLeftAnchor(listReplay, 50.0);
                AnchorPane.setTopAnchor(listReplay, 75.0);
                
                Button btnValidate = new Button();
                btnValidate.setText("Valider");
                AnchorPane.setLeftAnchor(btnValidate, 50.0);
                AnchorPane.setTopAnchor(btnValidate, 100.0);

                btnValidate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        if (!listReplay.getSelectionModel().isEmpty()){
                            replayPane.getChildren().clear();
                            ReplayViewer replay = new ReplayViewer(listReplay.getValue(), replayPane, main);
                            replay.play();
                        }
                    }
                });
                
                replayPane.getChildren().addAll(desc, cancelBtn, listReplay, btnValidate);

                Scene replay = new Scene(replayPane, windowWidth, WindowHeight);
                primaryStage.setScene(replay);
            }
        });

        primaryStage.setTitle("Home - Game4j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void reset(){
        this.primaryStage.setScene(this.scene);
    }
}