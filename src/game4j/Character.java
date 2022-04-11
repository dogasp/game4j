package game4j;


import java.util.ArrayList;
import java.util.List;

public class Character {
    private int energy;
    private int x;
    private int y;

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
        //System.out.println("dx: " + dx + "dy: " + dy);
        if (true){

        }
    }

}
