package game4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.layout.AnchorPane;

public class Game {
    private int date;
    private int timeStart;
    private Square[] squarelist;
    private AnchorPane pane;
    private int width;
    private int height;

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
            line = myReader.nextLine();
            this.height = Integer.parseInt(line);


            for(int i = 0; i < this.height; i ++){
                String tmp = myReader.nextLine();
                for (int j = 0; j < this.width; j ++){
                    System.out.print(tmp.charAt(j));
                }
                System.out.println();
            }
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              System.out.println(data);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


    public void loop(){

    }
}
