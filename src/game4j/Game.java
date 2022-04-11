package game4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.event.EventHandler;
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
    private float squareWidth;
    private float squareHeight;
    private int startEnergy;

    public Game(AnchorPane root){
        this.pane = root;
    }

    public void start() {
        //lecture de la map (on pourrait demande à l'utilisateur laquelle il veut utiliser)
        try {
            File myObj = new File("ressources/maps/test.map");
            Scanner myReader = new Scanner(myObj);
            String line = myReader.nextLine();
            this.width = Integer.parseInt(line);
            this.squareWidth = 400/this.width;
            line = myReader.nextLine();
            this.height = Integer.parseInt(line);
            this.squareHeight = 400/this.height;
            line = myReader.nextLine();
            this.startEnergy = Integer.parseInt(line);


            for(int i = 0; i < this.height; i ++){
                String tmp = myReader.nextLine();
                for (int j = 0; j < this.width; j ++){
                    squarelist.add(new Square(j, i, i*10+j, tmp.charAt(j)));
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

        this.player = new Character(this.startEnergy, this.start.getX(), this.start.getY());

        this.pane.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override public void handle( KeyEvent event ) {
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
            }
        }); 

        for (Square square : squarelist) {
            square.afficher(this.pane);
        }
        
    }
}
