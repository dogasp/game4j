package sample;


import java.util.ArrayList;
import java.util.List;

public class Character {
    private int startEnergy;
    private int currentEnergy;
    private List<Integer> charPos;
    private int xPos;
    private int yPos;

    private void iniPos(){
        charPos.add(xPos);
        charPos.add(yPos);
    }


    public int getStartEnergy() {
        return startEnergy;
    }

    public List<Integer> getCharPos() {
        return charPos;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public Character(int sE, int cE, List<Integer> cP){
        this.charPos=cP;
        this.currentEnergy=cE;
        this.startEnergy=sE;
    }

    public void setCharPos(int x, int y) {
        this.charPos.add(x);
        this.charPos.add(y);
    }

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public void move(String direction){
        switch(direction){
            case "left":
                this.setCharPos(this.getCharPos().get(0)-1, this.getCharPos().get(1));
                break;
            case "right":
                this.setCharPos(this.getCharPos().get(0)+1, this.getCharPos().get(1));
                break;
            case "up":
                this.setCharPos(this.getCharPos().get(0), this.getCharPos().get(1)+1);
                break;
            case "down":
                this.setCharPos(this.getCharPos().get(0), this.getCharPos().get(1)-1);
                break;

        }
    }


}
