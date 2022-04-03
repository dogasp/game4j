package sample;


import java.util.ArrayList;
import java.util.List;
public class Square {
    private List<Integer> squarePos;
    private String squareType;
    private int id;

    public Square(List<Integer> sPos, String sT, int id){
        this.squarePos=sPos;
        this.squareType=sT;
        this.id=id;
    }

    public List<Integer> getSquarePos() {
        return squarePos;
    }

    public String getSquareType() {
        return squareType;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getSouthSquare() {
        List<Integer> SouthSquarePos = new ArrayList<>();
        SouthSquarePos.add(this.squarePos.get(0)-1);
        SouthSquarePos.add(this.squarePos.get(1));
        return SouthSquarePos;
    }

    public List<Integer> getNorthSquare(){
        List<Integer> NorthSquarePos = new ArrayList<>();
        NorthSquarePos.add(this.squarePos.get(0)+1);
        NorthSquarePos.add(this.squarePos.get(1));
        return NorthSquarePos;
    }

    public List<Integer> getEastSquare(){
        List<Integer> EastSquarePos = new ArrayList<>();
        EastSquarePos.add(this.squarePos.get(1)+1);
        EastSquarePos.add(this.squarePos.get(0));
        return EastSquarePos;
    }

    public List<Integer> getWestSquare(){
        List<Integer> WestSquarePos = new ArrayList<>();
        WestSquarePos.add(this.squarePos.get(1)-1);
        WestSquarePos.add(this.squarePos.get(0));
        return WestSquarePos;
    }

    public double getDistance(Square square2){
        int x2 = square2.getSquarePos().get(0);
        int x1 = this.squarePos.get(0);
        int y2 = square2.getSquarePos().get(1);
        int y1 = this.squarePos.get(1);
        double d = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        return d;

    }


}
