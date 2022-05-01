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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

//Classe principale gérant le menu
public class Main extends Application {

    public static int windowWidth = 800; //défninition de la taille de la fenètre
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
                root.getChildren().clear();
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

        Button btnGenerate = new Button();
        btnGenerate.setText("Genérer map");
        AnchorPane.setTopAnchor(btnGenerate, 300.0);
        AnchorPane.setLeftAnchor(btnGenerate, 150.0);

        root.getChildren().addAll(btnStart, title, btnGenerate);

        this.scene = new Scene(root, windowWidth, WindowHeight);

        Main main = this; //pour l'envoyer dans le replay

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                AnchorPane generatePane = new AnchorPane();

                //label titre

                Label desc = new Label();
                desc.setText("Selectionnez les options pour la génération");
                AnchorPane.setTopAnchor(desc, 55.0);
                AnchorPane.setLeftAnchor(desc, 50.0);

                //bouton pour retourner au menu principal

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

                //label pur la largeur

                Label widthLabel = new Label();
                widthLabel.setText("Largeur de la grille");
                AnchorPane.setTopAnchor(widthLabel, 80.0);
                AnchorPane.setLeftAnchor(widthLabel, 50.0);

                //input pour entrer la valeur de la largeur

                TextField widthText = new TextField("6");
                widthText.setAccessibleText("6");
                widthText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    widthText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(widthText, 80.0);
                AnchorPane.setLeftAnchor(widthText, 210.0);

                //label pour la hauteur

                Label heightLabel = new Label();
                heightLabel.setText("Hauteur de la grille");
                AnchorPane.setTopAnchor(heightLabel, 110.0);
                AnchorPane.setLeftAnchor(heightLabel, 50.0);

                //input pour entrer la valeur de la hauteur

                TextField heightText = new TextField("6");
                heightText.setAccessibleText("6");
                heightText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    heightText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(heightText, 110.0);
                AnchorPane.setLeftAnchor(heightText, 210.0);

                //label pour la difficulté

                Label diffLabel = new Label();
                diffLabel.setText("Selectionnez difficultée");
                AnchorPane.setTopAnchor(diffLabel, 140.0);
                AnchorPane.setLeftAnchor(diffLabel, 50.0);

                //liste déroulante pour la sélection de la difficulté

                String diffTitle[] = {"Easy", "Medium", "Hard"};
                ComboBox<String> listDiff = new ComboBox<String>(FXCollections.observableArrayList(diffTitle));
                AnchorPane.setTopAnchor(listDiff, 140.0);
                AnchorPane.setLeftAnchor(listDiff, 210.0);

                //label sélection du nombre d'obstacle

                Label obstacleLabel = new Label();
                obstacleLabel.setText("Nombre d'obstacles");
                AnchorPane.setTopAnchor(obstacleLabel, 170.0);
                AnchorPane.setLeftAnchor(obstacleLabel, 50.0);

                //entrée du nombre d'obstacle

                TextField obstacleText = new TextField("4");
                obstacleText.setAccessibleText("4");
                obstacleText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    obstacleText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(obstacleText, 170.0);
                AnchorPane.setLeftAnchor(obstacleText, 210.0);

                //label entrée du nombre de bonnus

                Label bonusLabel = new Label();
                bonusLabel.setText("Nombre de bonnus");
                AnchorPane.setTopAnchor(bonusLabel, 200.0);
                AnchorPane.setLeftAnchor(bonusLabel, 50.0);

                //entrée du nombre de bonnus

                TextField bonusText = new TextField("2");
                bonusText.setAccessibleText("2");
                bonusText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    bonusText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(bonusText, 200.0);
                AnchorPane.setLeftAnchor(bonusText, 210.0);

                //label si on prend en compte l'energie

                Label energyLabel = new Label();
                energyLabel.setText("vérifier si possible (energie)");
                AnchorPane.setTopAnchor(energyLabel, 240.0);
                AnchorPane.setLeftAnchor(energyLabel, 50.0);

                //checkbox si on prend ou non en compte l'energie

                CheckBox energycb = new CheckBox();
                energycb.setSelected(true);
                AnchorPane.setTopAnchor(energycb, 240.0);
                AnchorPane.setLeftAnchor(energycb, 250.0);


                //Bouton pour lancer la génération
                
                Button btnValidate = new Button();
                btnValidate.setText("Generer");
                AnchorPane.setLeftAnchor(btnValidate, 50.0);
                AnchorPane.setTopAnchor(btnValidate, 280.0);

                btnValidate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        if (!listDiff.getSelectionModel().isEmpty()){ //check que tout est remplis
                            Generator generateur = new Generator();
                            if (generateur.generate(listDiff.getValue(), Integer.valueOf(widthText.getAccessibleText()), Integer.valueOf(heightText.getAccessibleText()), 
                                    Integer.valueOf(obstacleText.getAccessibleText()), Integer.valueOf(bonusText.getAccessibleText()), energycb.isSelected())){

                                generatePane.getChildren().clear();
                                Game game = new Game(generatePane);
                                game.gameFromGen(generateur);
                            }
                            else{
                                Label probleme = new Label();
                                probleme.setText("Impossible de générer une map, veuillez changer vos paramètres");
                                probleme.setTextFill(Paint.valueOf("#f23"));
                                AnchorPane.setTopAnchor(probleme, 300.0);
                                AnchorPane.setLeftAnchor(probleme, 50.0);
                                generatePane.getChildren().add(probleme);
                            }
                        }
                        else{
                            Label probleme = new Label();
                            probleme.setText("Tous les champs ne sont pas remplis");
                            probleme.setTextFill(Paint.valueOf("#f23"));
                            AnchorPane.setTopAnchor(probleme, 300.0);
                            AnchorPane.setLeftAnchor(probleme, 50.0);
                            generatePane.getChildren().add(probleme);
                        }
                    }
                });
                
                generatePane.getChildren().addAll(desc, cancelBtn, widthLabel, widthText, heightLabel, heightText, btnValidate, listDiff, diffLabel,
                obstacleLabel, obstacleText, bonusLabel, bonusText, energyLabel, energycb);

                Scene generate = new Scene(generatePane, windowWidth, WindowHeight);
                primaryStage.setScene(generate);
            }
        });

        btnReplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                AnchorPane replayPane = new AnchorPane();

                Label desc = new Label();
                desc.setText("Selectionnez un replay à lire");
                AnchorPane.setTopAnchor(desc, 55.0);
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

                Label speedLabel = new Label();
                speedLabel.setText("Selectionnez une vitesse de lecture");
                AnchorPane.setTopAnchor(speedLabel, 110.0);
                AnchorPane.setLeftAnchor(speedLabel, 50.0);

                String speedTitle[] = {"Slow", "Normal", "Quick"};
                ComboBox<String> listSpeed = new ComboBox<String>(FXCollections.observableArrayList(speedTitle));
                AnchorPane.setLeftAnchor(listSpeed, 50.0);
                AnchorPane.setTopAnchor(listSpeed, 130.0);
                
                Button btnValidate = new Button();
                btnValidate.setText("Valider");
                AnchorPane.setLeftAnchor(btnValidate, 50.0);
                AnchorPane.setTopAnchor(btnValidate, 160.0);

                btnValidate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        if (!listReplay.getSelectionModel().isEmpty()){
                            replayPane.getChildren().clear();
                            ReplayViewer replay = new ReplayViewer(listReplay.getValue(), replayPane, main, listSpeed.getValue());
                            replay.play();
                        }
                    }
                });
                
                replayPane.getChildren().addAll(desc, cancelBtn, listReplay, btnValidate, listSpeed, speedLabel);

                Scene replay = new Scene(replayPane, windowWidth, WindowHeight);
                primaryStage.setScene(replay);
            }
        });

        primaryStage.setTitle("Home - Game4j"); //change title when in menu or in game
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