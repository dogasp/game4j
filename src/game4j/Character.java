package game4j;


import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Character {
    private int energy;
    private int x;
    private int y;
    private Rectangle rendu;
    private int nbReturn; // nombre de retours en arrière (limite de 6)
    private List<Square> historiqueSquare = new ArrayList<Square>();

    public Character(int energy, int x, int y){
        this.x      = x;
        this.y      = y;
        this.energy = energy;
    }

    public int getnbReturn(){
        return this.nbReturn;
    }

    public void setnbReturn(int n){
        this.nbReturn = n;
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

            this.historiqueSquare.add(nextSquare);

            this.energy -= 1;
            switch (nextSquare.getSquareType()){
                case 'A':
                    this.Win();
                    break;
                case 'O':
                    this.energy -= 10;
                    this.historiqueSquare.add(this.historiqueSquare.get(this.historiqueSquare.size() - 2));
                    this.afficherBoucle();
                    return;
                case 'B':
                    this.energy += 10;
                    nextSquare.becomeVoid();
                default:
                    break;
            }

            this.x += dx;
            this.y += dy;
            
            TranslateTransition translate = new TranslateTransition(Duration.millis(150), this.rendu);
            translate.setToX(this.x*(Main.WindowHeight/10));
            translate.setToY(this.y*(Main.WindowHeight/10));
            translate.play();

            this.afficherBoucle();

            if (this.energy <= 0){
                this.Dead();
            }
        }

    }

    public void afficher(AnchorPane root){
        this.rendu = new Rectangle(this.x*(Main.WindowHeight/10)+(Main.WindowHeight/40), this.y*(Main.WindowHeight/10) + (Main.WindowHeight/40), (Main.WindowHeight/20), (Main.WindowHeight/20)); // taille à changer pour dynamic
        this.rendu.setFill(Color.BEIGE);

        root.getChildren().add(this.rendu);
    }

    public void initHistory(Square square){
        this.historiqueSquare.add(square);
    }

    public void afficherBoucle(){
        Square last = this.historiqueSquare.get(this.historiqueSquare.size() -1);
        for (int i = 0; i < this.historiqueSquare.size() -1; i ++) {
            if (last == this.historiqueSquare.get(i)){
                for (int j = i; j < this.historiqueSquare.size(); j ++){
                    System.out.print("[" + this.historiqueSquare.get(j).getX() + "; " + this.historiqueSquare.get(j).getY() + "]" + " - ");
                }
                break;
            }
        }
        System.out.println();
    }

    public void Win(){
        System.out.println("Victoire !");
    }

    public void Dead(){
        System.out.println("Perdu !");
    }

    public void cancel(){
        this.energy += 1;
        Square old = this.historiqueSquare.get(this.historiqueSquare.size() -1);
        switch (old.getSquareType()){
            case 'O':
                this.energy += 10;
                this.historiqueSquare.remove(this.historiqueSquare.get(this.historiqueSquare.size() -2));
                this.afficherBoucle();
                return;
            case 'V':
                if (old.wasBonus){
                    this.energy -= 10;
                    old.cancelVoid();
                }
            default:
                break;
        }
        this.x = this.historiqueSquare.get(this.historiqueSquare.size() -2).getX();
        this.y = this.historiqueSquare.get(this.historiqueSquare.size() -2).getY();

        this.historiqueSquare.remove(old);

        TranslateTransition translate = new TranslateTransition(Duration.millis(400), this.rendu); // créer fonction pour actualiser la position du joueur
        translate.setToX(this.x*(Main.WindowHeight/10));
        translate.setToY(this.y*(Main.WindowHeight/10));
        translate.play();

        System.out.println(this.historiqueSquare);
    }

}
