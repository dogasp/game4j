package game4j;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player {
    private int energy;
    private int x;
    private int y;
    private int EarnedEnergy;
    private int LostEnergy;
    private float squareLength;
    private Rectangle rendu;
    private int nbReturn; // nombre de retours en arrière (limite de 6)
    private List<Square> historiqueSquare = new ArrayList<Square>();

    public Player(int energy, int x, int y, float squareLength){
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.squareLength = squareLength;
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

    public int getEarnedEnergy(){
        return this.EarnedEnergy;
    }

    public void setEarnedEnergy(int n){
        this.EarnedEnergy = n;
    }

    public int getLostEnergy(){
        return this.LostEnergy;
    }

    public void setLostEnergy(int n){
        this.LostEnergy = n;
    }

    public List<Square> getHistorySquare(){
        return this.historiqueSquare;
    }

    public void setHistoriqueSquare(List<Square> list){
        this.historiqueSquare = list;
    }

    public int move(int dx, int dy, List<Square> squarelist, int width, int height){
        //retourne 0 si la partie continue, -1 si le joueur à perdu et 1 si le joueur à gagné
        if ((this.x + dx) < width && (this.x + dx) > -1 && (this.y + dy) < height && (this.y + dy) > -1){
            Square nextSquare = squarelist.get((this.y+dy)*width + (this.x+dx));
            //int distance = this.historiqueSquare.get(this.historiqueSquare.size() - 1).getDistance(nextSquare);

            this.historiqueSquare.add(nextSquare);

            this.energy -= 1;
            this.LostEnergy +=1;
            switch (nextSquare.getSquareType()){
                case 'A':
                    this.Win();
                    break;
                case 'O':
                    this.energy -= 10;
                    this.LostEnergy += 10;
                    this.historiqueSquare.add(this.historiqueSquare.get(this.historiqueSquare.size() - 2));
                    this.afficherBoucle();
                    return 0;
                case 'B':
                    this.energy += 10;
                    this.EarnedEnergy += 10;
                    nextSquare.becomeVoid();
                default:
                    break;
            }

            this.x += dx;
            this.y += dy;
            
            TranslateTransition translate = new TranslateTransition(Duration.millis(150), this.rendu);
            translate.setToX(this.x*this.squareLength);
            translate.setToY(this.y*this.squareLength);
            translate.play();

            if (nextSquare.getSquareType() == 'A'){
                return 1; //évite de devoir actualiser le personnage
            }

            this.afficherBoucle();

            if (this.energy <= 0){
                this.Dead();
                return -1;
            }
        }
        return 0;
    }

    public void afficher(AnchorPane root){
        this.rendu = new Rectangle(this.squareLength/4, this.squareLength/4, this.squareLength/2, this.squareLength/2); // taille à changer pour dynamic
        this.rendu.setFill(Color.BEIGE);

        root.getChildren().add(this.rendu);

        TranslateTransition translate = new TranslateTransition(Duration.millis(150), this.rendu);
        translate.setToX(this.x*this.squareLength);
        translate.setToY(this.y*this.squareLength);
        translate.play();
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
                System.out.println();
                break;
            }
        }
    }

    public void Win(){
        int distance = 0;
        for (int i = 0; i < this.historiqueSquare.size()-1; i++) {
            distance += this.historiqueSquare.get(i).getDistance(this.historiqueSquare.get(i+1));
        }
        System.out.println("Victoire !\nDistance parcourue: " + distance + "\nEnergie restante: " + this.energy + "\nEnergie gagnée: " + this.EarnedEnergy + "\nEnergie perdue: " + this.LostEnergy);

        System.out.print("Chemin parcouru: ");
        for (Square square : this.historiqueSquare) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println();
    }

    public void Dead(){
        System.out.println("Perdu !");
    }

    public void cancel(Label label){
        if (this.historiqueSquare.size() == 1){
            this.nbReturn += 1;
            return;
        }
        this.energy += 1;
        this.LostEnergy -=1;

        Square old = this.historiqueSquare.get(this.historiqueSquare.size() -2);
        Square current = this.historiqueSquare.get(this.historiqueSquare.size() - 1);

        if (old.getSquareType() == 'O'){
            this.energy += 10;
            this.LostEnergy += 10;
            this.historiqueSquare.remove(old);
            old = current;
        } else if (current.getSquareType() == 'V'){
            if (current.getWasBonnus()){
                this.energy -= 10;
                this.EarnedEnergy -= 10;
                current.cancelVoid();
            }
        }
        this.x = old.getX();
        this.y = old.getY();

        this.historiqueSquare.remove(current);

        TranslateTransition translate = new TranslateTransition(Duration.millis(400), this.rendu); // créer fonction pour actualiser la position du joueur
        translate.setToX(this.x*squareLength);
        translate.setToY(this.y*squareLength);
        translate.play();

        label.setText("Stamina : " + this.getEnergy());
    }

}
