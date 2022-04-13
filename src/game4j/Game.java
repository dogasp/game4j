package game4j;

import java.io.File;
import java.io.FileNotFoundException;
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
    private Character player;
    private int width;
    private int height;
    private float squareLength;
    private int startEnergy;
    private Label labelStamina;

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

        this.player = new Character(this.startEnergy, this.start.getX(), this.start.getY(), this.squareLength);
        this.labelStamina = new Label("Stamina : " + player.getEnergy()); //bouger label vers la classe Game
        AnchorPane.setRightAnchor(this.labelStamina, 100.0);

        this.pane.getChildren().add(this.labelStamina);

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle( KeyEvent event ) {
                switch(event.getCode()){ // ignorer le warning tout foncitonne
                    case UP:
                        player.move(0, -1, squarelist, width, height);
                        break;
                    case DOWN:
                        player.move(0, 1,  squarelist, width, height);
                        break;
                    case RIGHT:
                        player.move(1, 0,  squarelist, width, height);
                        break;
                    case LEFT:
                        player.move(-1, 0, squarelist, width, height);
                        break;
                }
                event.consume();
                
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

        this.pane.requestFocus(); //empeche que le bouton cancel prenne le focus et dérange la prise d'input

        this.pane.getChildren().add(cancelBtn);
    }
}
