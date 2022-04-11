package game4j;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;

public class Square {
    private int x;
    private int y;
    private int id;
    private char squareType; // V pour vide, B pour Bonus, O pour Obstacle, A pour Arrivée

    public Square(int x, int y, int id, char type){
        this.x = x;
        this.y = y;
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

    }


}
