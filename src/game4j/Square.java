package game4j;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
    private int x;
    private int y;
    private float width;
    private float height;
    private int id;
    boolean wasBonus = false;
    private char squareType; // V pour vide, B pour Bonus, O pour Obstacle, A pour Arrivée
    private Rectangle rendu;

    public Square(int x, int y, float width, float height, int id, char type){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.squareType = type;
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

    public int getId() {
        return this.id;
    }

    public List<Integer> getSouthSquare() {
        List<Integer> SouthSquarePos = new ArrayList<>();
        SouthSquarePos.add(this.x-1);
        SouthSquarePos.add(this.y);
        return SouthSquarePos;
    }

    public List<Integer> getNorthSquare(){
        List<Integer> NorthSquarePos = new ArrayList<>();
        NorthSquarePos.add(this.x+1);
        NorthSquarePos.add(this.y);
        return NorthSquarePos;
    }

    public List<Integer> getEastSquare(){
        List<Integer> EastSquarePos = new ArrayList<>();
        EastSquarePos.add(this.y+1);
        EastSquarePos.add(this.x);
        return EastSquarePos;
    }

    public List<Integer> getWestSquare(){
        List<Integer> WestSquarePos = new ArrayList<>();
        WestSquarePos.add(this.y-1);
        WestSquarePos.add(this.x);
        return WestSquarePos;
    }

    public double getDistance(Square square2){
        int x2 = square2.getX();
        int x1 = this.x;
        int y2 = square2.getY();
        int y1 = this.y;
        
        double d = Math.abs(x2-x1) + Math.abs(y2-y1); //distance de manhattan entre deux points
        return d;
    }

    public void afficher(AnchorPane root){
        this.rendu = new Rectangle(this.x*this.width, this.y*this.height, this.width, this.height);
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
