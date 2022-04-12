package game4j;


import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Character {
    private int energy;
    private int x;
    private int y;
    private Rectangle rendu;
    private Label labelStamina;

    public Character(int energy, int x, int y){
        this.x      = x;
        this.y      = y;
        this.energy = energy;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return this.x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return this.y;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void move(int dx, int dy, List<Square> squarelist, int width, int height){
        if ((this.x + dx) < width && (this.x + dx) > -1 && (this.y + dy) < height && (this.y + dy) > -1){
            Square nextSquare = squarelist.get((this.y+dy)*10 + (this.x+dx));

            this.energy -= 1;
            switch (nextSquare.getSquareType()){
                case 'A':
                    this.Win();
                    break;
                case 'O':
                    this.energy -= 10;
                    return;
                case 'B':
                    this.energy += 10;
                    nextSquare.becomeVoid();
                default:
                    break;
            }
            this.labelStamina.setText("Stamina : " + this.energy);

            this.x += dx;
            this.y += dy;
            
            TranslateTransition translate = new TranslateTransition(Duration.millis(150), this.rendu);
            translate.setToX(this.x*(Main.WindowHeight/10));
            translate.setToY(this.y*(Main.WindowHeight/10));
            translate.play();

            if (this.energy <= 0){
                this.Dead();
            }
        }

    }

    public void afficher(AnchorPane root){
        this.rendu = new Rectangle(this.x*(Main.WindowHeight/10)+(Main.WindowHeight/40), this.y*(Main.WindowHeight/10) + (Main.WindowHeight/40), (Main.WindowHeight/20), (Main.WindowHeight/20)); // taille à changer pour dynamic
        this.rendu.setFill(Color.BEIGE);

        this.labelStamina = new Label("Stamina : " + this.energy);
        AnchorPane.setRightAnchor(this.labelStamina, 100.0);

        root.getChildren().addAll(this.rendu, this.labelStamina);
    }

    public void Win(){
        System.out.println("Victoire !");
    }

    public void Dead(){
        System.out.println("Perdu !");
    }

}
