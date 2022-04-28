package game4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Game {
    private int date;
    private int timeStart;
    private List<Square> squarelist = new ArrayList<Square>(); // Liste des cases
    /*private List<Food> foodList = new ArrayList<Food>(); // liste des cases avec de la nourriture
    private List<Obstacle> obstableList = new ArrayList<Obstacle>();*/
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

    public Game(AnchorPane root){
        this.pane = root;
    }

    public void loadMap(String file){
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            String line = myReader.nextLine();
            this.width = Integer.parseInt(line);

            this.squareLength = Main.WindowHeight/this.width;
            line = myReader.nextLine();
            this.height = Integer.parseInt(line);

            line = myReader.nextLine();
            this.startEnergy = Integer.parseInt(line);

            for(int i = 0; i < this.height; i ++){
                for (int j = 0; j < this.width; j ++){
                    String tmp = myReader.nextLine();
                    String[] data = tmp.split(" ");
                    squarelist.add(new Square(j, i, this.squareLength, i*this.width+j, this.width, data[0], Arrays.copyOfRange(data, 1, 5)));
                    if (tmp.charAt(0) == 'D'){
                        this.start = squarelist.get(j);
                    }
                    else if (tmp.charAt(0) == 'A') {
                        this.finish = squarelist.get(j);
                    }
                }
                squarelist.get(0).getX();
            }
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              System.out.println(data);
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

    public void start() {
        //lecture de la map (on pourrait demande à l'utilisateur laquelle il veut utiliser)

        this.labelStamina = new Label("Stamina : " + this.player.getEnergy()); //bouger label vers la classe Game
        AnchorPane.setRightAnchor(this.labelStamina, 100.0);

        this.labelCancel = new Label("Cancel chance : " + (6-this.player.getnbReturn()));
        AnchorPane.setRightAnchor(this.labelCancel, 80.0);
        AnchorPane.setTopAnchor(this.labelCancel, 20.0);

        this.pane.getChildren().addAll(this.labelStamina, this.labelCancel);

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle( KeyEvent event ) {
                int resultat = 0;
                switch(event.getCode()){ // ignorer le warning tout foncitonne
                    case UP:
                        resultat = player.move(0, -1, squarelist, width, height);
                        break;
                    case DOWN:
                        resultat = player.move(0, 1,  squarelist, width, height);
                        break;
                    case RIGHT:
                        resultat = player.move(1, 0,  squarelist, width, height);
                        break;
                    case LEFT:
                        resultat = player.move(-1, 0, squarelist, width, height);
                        break;
                }
                event.consume();
                if (resultat != 0){
                    if (resultat == 1){
                        asWin();
                    }
                    LocalDateTime now = LocalDateTime.now();
                    saveGame("ressources/replay/" + DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(now)+".dat");
                }

                
                
                labelStamina.setText("Stamina : " + player.getEnergy()); // actualisation de la stamina
            }
        }); 

        for (Square square : squarelist) {
            square.afficher(this.pane);
        }
        
        this.cancelBtn = new Button();
        this.cancelBtn.setText("Cancel move");
        AnchorPane.setBottomAnchor(this.cancelBtn, 50.0);
        AnchorPane.setRightAnchor(this.cancelBtn, 90.0);

        this.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (player.getnbReturn() < 6){
                    player.setnbReturn(player.getnbReturn()+1);
                    player.cancel(labelStamina);
                }
                labelCancel.setText("Cancel chance : " + (6-player.getnbReturn()));
                pane.requestFocus();
            }
        });

        this.saveButton = new Button();
        saveButton.setText("Sauvegarder partie");
        AnchorPane.setBottomAnchor(this.saveButton, 100.0);
        AnchorPane.setRightAnchor(this.saveButton, 40.0);

        this.player.afficher(pane);

        this.saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                saveGame("ressources/saves/sauvegarde.dat");
            }
        });

        this.pane.requestFocus(); //empeche que le bouton cancel prenne le focus et dérange la prise d'input

        this.pane.getChildren().addAll(cancelBtn, saveButton);
    }

    public void asWin(){
        //fonction pour lancer la recherche de meilleur chemin quand le joueur à gagné la partie
        DijkstraAlgo algo = new DijkstraAlgo(this.squarelist, this.width);
        algo.optimiserDistance(this.start, this.finish);

        MinMaxAlgo pathFinder = new MinMaxAlgo(this.start, this.width, this.height, this.startEnergy);
        //pathFinder.findBestPathEnergy(this.squarelist);
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
        this.labelStamina.setText("Stamina : " + this.startEnergy);
        this.pane.getChildren().remove(this.cancelBtn);
        this.pane.getChildren().remove(this.saveButton);

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle( KeyEvent event ) {

            }
        });
    }
}
