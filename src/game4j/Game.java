package game4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class Game {
    private List<Square> squarelist = new ArrayList<Square>(); // Liste des cases
    private Square start;
    private Square finish;
    private AnchorPane pane;
    private Player player;
    private int width;
    private int height;
    private float squareLength;
    private int startEnergy;
    private Label labelStamina;
    private Label labelCancel;
    private Button cancelBtn;
    private Button saveButton;
    private Button menuButton;
    public Object KeyEvent;
    public Main main;

    public Game(AnchorPane root, Main main){
        this.pane = root;
        this.main = main;
    }

    public void loadMap(String file){
        //fonction pour charger une map à partir d'un fichier
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj); //lecteur
            String line = myReader.nextLine(); //lecture de la largeur
            this.width = Integer.parseInt(line);

            this.squareLength = Main.WindowHeight/(this.width>this.height?this.width:this.height);
            line = myReader.nextLine();
            this.height = Integer.parseInt(line); //lecture de la hauteur

            line = myReader.nextLine();
            this.startEnergy = Integer.parseInt(line);

            for(int i = 0; i < this.height; i ++){ //pour chaque ligne
                for (int j = 0; j < this.width; j ++){ //pour chaque colone
                    String tmp = myReader.nextLine(); //lecture du fichier
                    String[] data = tmp.split(" "); //les données sont séparées par des espaces
                    this.squarelist.add(new Square(j, i, this.squareLength, i*this.width+j, data[0], Arrays.copyOfRange(data, 1, 5))); //création de la case correspondante
                    if (tmp.charAt(0) == 'D'){
                        this.start = squarelist.get(j); //si c'est le départ, on le retient
                    }
                    else if (tmp.charAt(0) == 'A') {
                        this.finish = this.squarelist.get(j); //même chose pour l'arrivée
                    }
                }
            }
            myReader.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        //initialisation du joueur avec les infos récupérées
        this.player = new Player(this.startEnergy, this.start.getX(), this.start.getY(), this.squareLength);
        player.initHistory(this.start);
        this.start();
    }

    public void gameFromGen(Generator generator){
        //création d'une partie à partir d'une generation
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.startEnergy = generator.getStartEnergy();
        this.squarelist = generator.getSquareList();
        this.start = generator.getStart();
        this.finish = generator.getFinish();

        this.squareLength = Main.WindowHeight/(this.width>this.height?this.width:this.height); //calcul de la taille suivant s'il y a le plus de lignes ou de colonnes

        this.player = new Player(this.startEnergy, this.start.getX(), this.start.getY(), this.squareLength);
        player.initHistory(this.start);

        this.start();
    }

    public void start() {

        //création des éléments informatifs
        this.pane.setStyle("-fx-background-color: #3E3E3E;");

        this.labelStamina = new Label("Energie : " + this.player.getEnergy());
        this.labelStamina.setStyle("-fx-text-fill: #DDD;-fx-font: 24 arial;");
        AnchorPane.setRightAnchor(this.labelStamina, 50.0);

        this.labelCancel = new Label("Annulations restantes : " + (6-this.player.getnbReturn()));
        this.labelCancel.setStyle("-fx-text-fill: #DDD;-fx-font: 14 arial;");
        AnchorPane.setRightAnchor(this.labelCancel, 20.0);
        AnchorPane.setTopAnchor(this.labelCancel, 40.0);

        this.pane.getChildren().addAll(this.labelStamina, this.labelCancel);

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            //gestion du clavier
            @Override
            public void handle( KeyEvent event ) {
                int resultat = 0;
                switch(event.getCode()){ //action suivant la touche pressée
                    case UP:
                        resultat = player.move(0, -1, squarelist, width, height, "UP");
                        break;
                    case DOWN:
                        resultat = player.move(0, 1,  squarelist, width, height, "DOWN");
                        break;
                    case RIGHT:
                        resultat = player.move(1, 0,  squarelist, width, height, "RIGHT");
                        break;
                    case LEFT:
                        resultat = player.move(-1, 0, squarelist, width, height, "LEFT");
                        break;
                    default:
                        break;
                }
                event.consume();
                if (resultat != 0){ // résultat nous dit si le joueur à perdu ou gagné ou est encore en partie
                    if (resultat == 1){
                        asWin(); //si le joueur à gagné, on appelle la fonction de fin
                    }
                    else{
                        //s'il a perdu, on affiche un message
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        AnchorPane dialogVbox = new AnchorPane();
                        dialogVbox.getChildren().add(new Label("Perdu !"));
                        Scene dialogScene = new Scene(dialogVbox, 100, 100);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                    LocalDateTime now = LocalDateTime.now();
                    saveGame("ressources/replay/" + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(now)+".dat"); //sauvegarde de la partie
                    main.reset(); //retour au menu principal
                }

                
                updateStaminaLabel(player.getEnergy()); // actualisation de la stamina
            }
        }); 

        for (Square square : squarelist) {
            square.afficher(this.pane); //affichage de toutes les cases
        }
        
        this.cancelBtn = new Button();
        this.cancelBtn.setText("Annuler déplacement");
        AnchorPane.setBottomAnchor(this.cancelBtn, 70.0);
        AnchorPane.setRightAnchor(this.cancelBtn, 45.0);

        this.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (player.getnbReturn() < 6){ //si l'utilisateur n'as pas utilisé toutes ses chances de retour arrière
                    player.setnbReturn(player.getnbReturn()+1); //changement du nombre de retours
                    player.cancel(labelStamina); //annulation du mouvement
                }
                labelCancel.setText("Annulations restantes : " + (6-player.getnbReturn()));
                pane.requestFocus();
            }
        });

        this.saveButton = new Button();
        saveButton.setText("Sauvegarder partie");
        AnchorPane.setBottomAnchor(this.saveButton, 110.0);
        AnchorPane.setRightAnchor(this.saveButton, 50.0);
        this.saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                saveGame("ressources/saves/sauvegarde.dat"); //sauvegarde dans le fichier
                main.reset();
            }
        });

        this.menuButton = new Button();
        menuButton.setText("Retour menu");
        AnchorPane.setBottomAnchor(this.menuButton, 30.0);
        AnchorPane.setRightAnchor(this.menuButton, 70.0);
        this.menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                main.reset();
            }
        });

        this.player.afficher(pane);


        this.pane.requestFocus(); //empeche que le bouton cancel prenne le focus et dérange la prise d'input

        this.pane.getChildren().addAll(cancelBtn, saveButton, menuButton);
    }

    public void asWin(){
        //fonction pour lancer la recherche de meilleur chemin quand le joueur à gagné la partie
        DijkstraAlgo bestDist = new DijkstraAlgo(this.squarelist, this.width, this.height).optimiserDistance(this.start, this.finish, false);

        System.out.print("Chemin le plus optimal niveau distance: ");
        for (Square square : bestDist.getPath()) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println("\nCela revient à parcourir " + bestDist.getValue());

        // meilleure energie avec parcours graphe

        EnergyAlgo res = (new EnergyAlgo(null, 0)).findBestPathEnergy(this.squarelist, this.width, this.height, this.startEnergy, this.start, true);

        System.out.print(" - Chemin le plus optimal niveau energie (parcours graphe): ");
        for (Square square : res.getHist()) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println("\nCela revient à " + res.getStamina() + " energies");



        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL); //affichage de la victoire
        AnchorPane dialogVbox = new AnchorPane();
        dialogVbox.getChildren().add(new Label("Victoire !"));
        Scene dialogScene = new Scene(dialogVbox, 700, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void saveGame(String location){
        //fonction pour sauvegarder la map dans un fichier sous ressources/saves
        //il faut stocker les infos des cases, du joueur et de la partie
        try (FileOutputStream fos = new FileOutputStream(location);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // write object to file
            Sauvegarde save = new Sauvegarde(this, this.squarelist, this.player);
            oos.writeObject(save);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadGame(String fileName){
        //fonction pour charger une partie qui a été sauvegardée
        try{
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            //read object from file
            Sauvegarde save = (Sauvegarde)in.readObject();
            in.close();
            file.close();
            save.load(this);
        }catch (IOException ex) { //gestion des exceptions
            ex.printStackTrace();
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        this.start();
    }

    public float getSquareWidth(){
        return this.squareLength;
    }

    public int getGridHeight(){
        return this.height;
    }

    public int getGridWidth(){
        return this.width;
    }

    public Square getStart(){
        return this.start;
    }

    public Square getFinish(){
        return this.finish;
    }

    public int getStartEnergy(){
        return this.startEnergy;
    }

    public void setSquareLength(float n){
        this.squareLength = n;
    }

    public void setGrid(int height, int width){
        this.width = width;
        this.height = height;
    }

    public void setSquareList(List<Square> listesquare){
        this.squarelist = listesquare;
    }

    public void setStart(int id){
        for (Square square : this.squarelist) {
            if (square.getId() == id){
                this.start = square;
            }
        }
    }

    public void setFinish(int id){
        for (Square square : this.squarelist) {
            if (square.getId() == id){
                this.finish = square;
            }
        }
    }

    public void setStartEnergy(int n){
        this.startEnergy = n;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void resetInterface(){
        //utilisé dans les replays pour masquer certaines infos et boutons
        this.updateStaminaLabel(this.startEnergy);
        this.pane.getChildren().removeAll(this.cancelBtn, this.saveButton, this.labelCancel);

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle( KeyEvent event ) {

            }
        });
    }

    public Player getPlayer(){
        return this.player;
    }

    public void updateStaminaLabel(int n){
        this.labelStamina.setText("Energie : " + n);
    }
}
