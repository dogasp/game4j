package game4j;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
    private int x;
    private int y;
    private float width;
    private int id;
    private boolean wasBonus = false;
    private char squareType; // V pour vide, B pour Bonus, O pour Obstacle, A pour Arrivée
    private Rectangle rendu;
    private int[] distances = new int[4]; //up right down left

    public Square(int x, int y, float width, int id, String type, String[] distances){
        this.x = x;
        this.y = y;
        this.width = width;
        this.id = id;
        this.squareType = type.charAt(0);
        for (int i = 0; i < 4; i ++){
            this.distances[i] = Integer.parseInt(distances[i]);
        }
    }

    public Square(int x, int y, float width, int id, Character type, int[] distances){
        this.x = x;
        this.y = y;
        this.width = width;
        this.id = id;
        this.squareType = type;
        this.distances = distances;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public char getSquareType() {
        return this.squareType;
    }

    public void setType(char type){
        this.squareType = type;
    }

    public int getId() {
        return this.id;
    }

    public boolean getWasBonnus(){
        return this.wasBonus;
    }

    public int[] getDistanceList(){
        return this.distances;
    }

    public int getDistance(Square square2){
        int distance = -1;
        for (int i = 0; i < 4; i ++){
            switch (i){
                case 0:
                    if ((this.x == square2.getX()) && (this.y-1 == square2.getY())){
                        distance = this.distances[i];
                    }
                    break;
                case 1:
                    if ((this.x+1 == square2.getX()) && (this.y == square2.getY())){
                        distance = this.distances[i];
                    }
                    break;
                case 2:
                    if ((this.x == square2.getX()) && (this.y+1 == square2.getY())){
                        distance = this.distances[i];
                    }
                    break;
                default:
                    if ((this.x-1 == square2.getX()) && (this.y == square2.getY())){
                        distance = this.distances[i];
                    }
                    break;
            }
        }
        return distance;
    }

    public void afficher(AnchorPane root){
        this.rendu = new Rectangle(this.x*this.width, this.y*this.width, this.width, this.width);
        switch (this.squareType){
            case 'V':
                this.rendu.setFill(Color.GRAY);
                break;
            case 'D':
                this.rendu.setFill(Color.BLUEVIOLET);
                break;
            case 'A':
                this.rendu.setFill(Color.RED);
                break;
            case 'O':
                this.rendu.setFill(Color.GREEN);
                break;
            case 'B':
                this.rendu.setFill(Color.YELLOW);
        }
        root.getChildren().add(this.rendu);
    }

    public void becomeVoid(){
        this.squareType = 'V';
        this.rendu.setFill(Color.GRAY);
        this.wasBonus = true;
    }

    public void cancelVoid(){
        this.squareType = 'B';
        this.rendu.setFill(Color.YELLOW);
        this.wasBonus = false;
    }
}
