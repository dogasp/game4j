package game4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

public class Game implements Serializable{
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
    private Boolean isFinished;

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
                    if (tmp.charAt(j) == 'D'){
                        this.start = squarelist.get(j);
                    }
                    else if (tmp.charAt(j) == 'A') {
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
    }

    public void start() {
        //lecture de la map (on pourrait demande à l'utilisateur laquelle il veut utiliser)
        loadMap("ressources/maps/test.map");

        this.player = new Player(this.startEnergy, this.start.getX(), this.start.getY(), this.squareLength);
        this.labelStamina = new Label("Stamina : " + player.getEnergy()); //bouger label vers la classe Game
        AnchorPane.setRightAnchor(this.labelStamina, 100.0);

        this.pane.getChildren().add(this.labelStamina);

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
                    isFinished = true;
                    if (resultat == 1){
                        asWin();
                    }
                }

                
                
                labelStamina.setText("Stamina : " + player.getEnergy()); // actualisation de la stamina
            }
        }); 

        for (Square square : squarelist) {
            square.afficher(this.pane);
        }

        player.afficher(this.pane);

        player.initHistory(this.start);

        Button cancelBtn = new Button();
        cancelBtn.setText("Cancel move");
        AnchorPane.setBottomAnchor(cancelBtn, 50.0);
        AnchorPane.setRightAnchor(cancelBtn, 90.0);

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (player.getnbReturn() < 6){
                    player.setnbReturn(player.getnbReturn()+1);
                }
                player.cancel(labelStamina);
                pane.requestFocus();
            }
        });

        Button saveButton = new Button();
        saveButton.setText("Sauvegarder partie");
        AnchorPane.setBottomAnchor(saveButton, 100.0);
        AnchorPane.setRightAnchor(saveButton, 40.0);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                saveGame();
            }
        });

        this.pane.requestFocus(); //empeche que le bouton cancel prenne le focus et dérange la prise d'input

        this.pane.getChildren().addAll(cancelBtn, saveButton);
    }

    public void asWin(){
        //fonction pour lancer la recherche de meilleur chemin quand le joueur à gagné la partie

    }

    public void saveGame(){
        //fonction pour sauvegarder la map dans un fichier sous ressources/saves
        //il faut stocker les infos des cases, du joueur et de la partie
        try (FileOutputStream fos = new FileOutputStream("object.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // write object to file
            oos.writeObject(this);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
}
