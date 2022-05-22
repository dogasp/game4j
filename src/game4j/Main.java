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
import javafx.scene.control.TextField;
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
        /*Interface de bienvenue, on peut lancer une partie de différente façon*/
        this.primaryStage = primaryStage; //récupération de la fenêtre

        AnchorPane root = new AnchorPane(); //création d'un panneau sur lequel il va y avoir les options de jeu
        root.setStyle("-fx-background-color: #3E3E3E;"); //arrière plan plus beau que du simple blanc

        Main main = this; //sauvegarde de l'objet main pour pouvoir l'envoyer plus tard dans les Handler

        Button btnStart = new Button(); //création d'un bouton pour lancer une partie avec une map située à ressources/maps/test.map
        btnStart.setText("Lancer Jeu (map de test)");
        AnchorPane.setTopAnchor(btnStart, 150.0);
        AnchorPane.setLeftAnchor(btnStart, 520.0);
        
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            //gestion du click sur le bouton pour lancer une partie
            @Override
            public void handle(ActionEvent event) {
                AnchorPane gamePane = new AnchorPane(); //création d'un nouveau panneau
                Game game = new Game(gamePane, main);   //création de l'objet du Jeu

                Scene gameScene = new Scene(gamePane, windowWidth, WindowHeight); //création d'une scène à partir du panneau
                primaryStage.setScene(gameScene); //rempolacement de la scène du menu par la scene de jeu
                game.loadMap("ressources/maps/test.dat"); //lacement de la partie à partir de la map sauvegardée
            }
        });

        
        Button btnLoad = new Button(); //bouton  de chargement de la partie sauvegardée (si elle existe)
        AnchorPane.setTopAnchor(btnLoad, 300.0);
        AnchorPane.setLeftAnchor(btnLoad, 475.0);

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                AnchorPane gamePane = new AnchorPane(); //cf précédent bouton pour le détail
                Game game = new Game(gamePane, main);
                Scene gameScene = new Scene(gamePane, windowWidth, WindowHeight);
                primaryStage.setScene(gameScene);
                game.loadGame("ressources/saves/sauvegarde.dat"); //lancement à partir du fichier de sauvegarde
            }
        });

        File file = new File("ressources/saves/sauvegarde.dat");
        if (file.exists()){ //si le fichier de sauvegarde existe
            Path path = Paths.get("ressources/saves/sauvegarde.dat");
            try {
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                
                LocalDateTime localDateTime = attr.creationTime() //récupération de al date de création
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
                btnLoad.setText("Charger partie du " + localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss"))); //définition du texte du boutton
            } catch (IOException e) {
                e.printStackTrace();
            }

            root.getChildren().add(btnLoad); //affichage du bouton
        }

        Label title = new Label(); //label titre juste pour la déco
        title.setText("Game4j");
        title.setStyle("-fx-font: 40 arial; -fx-text-fill: #DDD;-fx-font-weight: bold");
        AnchorPane.setTopAnchor(title, 225.0);
        AnchorPane.setLeftAnchor(title, 180.0);


        Button btnReplay = new Button();
        btnReplay.setText("Charger un replay");
        AnchorPane.setTopAnchor(btnReplay, 250.0);
        AnchorPane.setLeftAnchor(btnReplay, 535.0);

        if (Paths.get("ressources/replay").toFile().listFiles().length > 0){
            root.getChildren().add(btnReplay); //s'il existe au moins un replay, on affiche le bouton
        }

        Button btnGenerate = new Button();
        btnGenerate.setText("Genérer map");
        AnchorPane.setTopAnchor(btnGenerate, 200.0);
        AnchorPane.setLeftAnchor(btnGenerate, 550.0);

        root.getChildren().addAll(btnStart, title, btnGenerate); //ajout des éléments 

        this.scene = new Scene(root, windowWidth, WindowHeight); //création de la scène

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            //action faite lorsque le bouton generer est cliqué
            @Override
            public void handle(ActionEvent event){
                AnchorPane generatePane = new AnchorPane(); //création d'un nouveau panneau avec les informations sur la génération
                generatePane.setStyle("-fx-background-color: #3E3E3E;");

                //label titre

                Label desc = new Label();
                desc.setText("Selectionnez les options pour la génération");
                desc.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(desc, 50.0);
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
                widthLabel.setStyle("-fx-text-fill: #DDD; ");
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
                heightLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(heightLabel, 120.0);
                AnchorPane.setLeftAnchor(heightLabel, 50.0);

                //input pour entrer la valeur de la hauteur

                TextField heightText = new TextField("6");
                heightText.setAccessibleText("6");
                heightText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    heightText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(heightText, 120.0);
                AnchorPane.setLeftAnchor(heightText, 210.0);

                //label pour la difficulté

                Label diffLabel = new Label();
                diffLabel.setText("Selectionnez difficultée");
                diffLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(diffLabel, 160.0);
                AnchorPane.setLeftAnchor(diffLabel, 50.0);

                //liste déroulante pour la sélection de la difficulté

                String diffTitle[] = {"Easy", "Medium", "Hard"};
                ComboBox<String> listDiff = new ComboBox<String>(FXCollections.observableArrayList(diffTitle));
                AnchorPane.setTopAnchor(listDiff, 160.0);
                AnchorPane.setLeftAnchor(listDiff, 210.0);

                //label sélection du nombre d'obstacle

                Label obstacleLabel = new Label();
                obstacleLabel.setText("Nombre d'obstacles");
                obstacleLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(obstacleLabel, 200.0);
                AnchorPane.setLeftAnchor(obstacleLabel, 50.0);

                //entrée du nombre d'obstacle

                TextField obstacleText = new TextField("4");
                obstacleText.setAccessibleText("4");
                obstacleText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    obstacleText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(obstacleText, 200.0);
                AnchorPane.setLeftAnchor(obstacleText, 210.0);

                //label entrée du nombre de bonnus

                Label bonusLabel = new Label();
                bonusLabel.setText("Nombre de bonnus");
                bonusLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(bonusLabel, 240.0);
                AnchorPane.setLeftAnchor(bonusLabel, 50.0);

                //entrée du nombre de bonnus

                TextField bonusText = new TextField("2");
                bonusText.setAccessibleText("2");
                bonusText.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) return;
                    bonusText.setAccessibleText(newValue.replaceAll("[^\\d]", ""));
                });
                AnchorPane.setTopAnchor(bonusText, 240.0);
                AnchorPane.setLeftAnchor(bonusText, 210.0);

                //label si on prend en compte l'energie

                Label energyLabel = new Label();
                energyLabel.setText("vérifier si possible (energie)");
                energyLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(energyLabel, 280.0);
                AnchorPane.setLeftAnchor(energyLabel, 50.0);

                //checkbox si on prend ou non en compte l'energie

                CheckBox energycb = new CheckBox();
                energycb.setSelected(true);
                AnchorPane.setTopAnchor(energycb, 280.0);
                AnchorPane.setLeftAnchor(energycb, 250.0);


                //Bouton pour lancer la génération
                
                Button btnValidate = new Button();
                btnValidate.setText("Generer");
                AnchorPane.setLeftAnchor(btnValidate, 50.0);
                AnchorPane.setTopAnchor(btnValidate, 350.0);

                btnValidate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        if (!listDiff.getSelectionModel().isEmpty()){ //check que tout est remplis
                            Generator generateur = new Generator();
                            //test de génération de la map
                            if (generateur.generate(listDiff.getValue(), Integer.valueOf(widthText.getAccessibleText()), Integer.valueOf(heightText.getAccessibleText()), 
                                    Integer.valueOf(obstacleText.getAccessibleText()), Integer.valueOf(bonusText.getAccessibleText()), energycb.isSelected())){

                                AnchorPane gamePane = new AnchorPane();
                                Game game = new Game(gamePane, main);
                                Scene gameScene = new Scene(gamePane, windowWidth, WindowHeight);
                                primaryStage.setScene(gameScene);
                                game.gameFromGen(generateur); //s'il arrive à generer, on crée un objet de jeu et on lance la partie
                            }
                            else{
                                //s'il n'est pas possible de generer une map (20 essais) un message est affiché à l'utilisateur
                                Label probleme = new Label();
                                probleme.setText("Impossible de générer une map, veuillez changer vos paramètres");
                                probleme.setTextFill(Paint.valueOf("#f23"));
                                AnchorPane.setTopAnchor(probleme, 300.0);
                                AnchorPane.setLeftAnchor(probleme, 50.0);
                                generatePane.getChildren().add(probleme);
                            }
                        }
                        else{
                            //si tous les champs ne sont pas remplis, on informe l'utilisateur
                            Label probleme = new Label();
                            probleme.setText("Tous les champs ne sont pas remplis");
                            probleme.setTextFill(Paint.valueOf("#f23"));
                            AnchorPane.setTopAnchor(probleme, 300.0);
                            AnchorPane.setLeftAnchor(probleme, 50.0);
                            generatePane.getChildren().add(probleme);
                        }
                    }
                });
                
                //ajout de tous les elements pour les options de génération
                generatePane.getChildren().addAll(desc, cancelBtn, widthLabel, widthText, heightLabel, heightText, btnValidate, listDiff, diffLabel,
                obstacleLabel, obstacleText, bonusLabel, bonusText, energyLabel, energycb);

                Scene generate = new Scene(generatePane, windowWidth, WindowHeight);
                primaryStage.setScene(generate); //changment de scène pour afficher les options de génération
            }
        });

        btnReplay.setOnAction(new EventHandler<ActionEvent>() {
            //fonction executée lors d'un click sur le bouton pour acceder aux replays
            @Override
            public void handle(ActionEvent event){
                AnchorPane replayPane = new AnchorPane(); //création d'un menu pour selectionner le replay qu'on veut voir et la vitesse
                replayPane.setStyle("-fx-background-color: #3E3E3E;");

                //label selection du replay
                Label desc = new Label();
                desc.setText("Selectionnez un replay à lire");
                desc.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(desc, 60.0);
                AnchorPane.setLeftAnchor(desc, 50.0);

                Button cancelBtn = new Button();
                cancelBtn.setText("Cancel");
                AnchorPane.setBottomAnchor(cancelBtn, 20.0);
                AnchorPane.setLeftAnchor(cancelBtn, 50.0);

                cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        primaryStage.setScene(scene); //pour annuler, on reaffiche le panneau du menu
                    }
                });

                String elt[];

                File dir = new File("ressources/replay");
                elt = dir.list(); //liste des replays du dossier correspondant

                ComboBox<String> listReplay = new ComboBox<String>(FXCollections.observableArrayList(elt)); //liste défilante des replays visionnable
                AnchorPane.setLeftAnchor(listReplay, 50.0);
                AnchorPane.setTopAnchor(listReplay, 90.0);

                Label speedLabel = new Label();
                speedLabel.setText("Selectionnez une vitesse de lecture");
                speedLabel.setStyle("-fx-text-fill: #DDD; ");
                AnchorPane.setTopAnchor(speedLabel, 140.0);
                AnchorPane.setLeftAnchor(speedLabel, 50.0);

                String speedTitle[] = {"Slow", "Normal", "Quick"};
                ComboBox<String> listSpeed = new ComboBox<String>(FXCollections.observableArrayList(speedTitle));
                AnchorPane.setLeftAnchor(listSpeed, 50.0);
                AnchorPane.setTopAnchor(listSpeed, 170.0);
                
                Button btnValidate = new Button();
                btnValidate.setText("Valider");
                AnchorPane.setLeftAnchor(btnValidate, 50.0);
                AnchorPane.setTopAnchor(btnValidate, 250.0);

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

        primaryStage.setTitle("Game4j");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void reset(){
        this.primaryStage.setScene(this.scene); //affichage du panneau avec le menu
    }
}