package game4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnergyAlgo {
    private List<Square> history;
    private int stamina;

    //mauvais algorithme, juste un parcours de graphe suffit
    public EnergyAlgo(List<Square> squareHist, int stamina){
        this.history = squareHist;
        this.stamina = stamina;
    }

    public EnergyAlgo findBestPathEnergy(List<Square> squareList, int width, int height, int startEnergy, Square start, Boolean optiEnergy){

        //on ne peut pas avoir de trajet demandant une mana négative, sinon c'est que le chemin n'est pas possible
        List<Square> hist = new ArrayList<Square>();
        hist.add(start);
        
        EnergyAlgo res = this.recursive(hist, squareList, startEnergy, width, height, optiEnergy);

        return res;
    }

    private EnergyAlgo recursive(List<Square> historique, List<Square> squareList, int energy, int gridWidth, int gridHeight, Boolean optiEnergy){
        EnergyAlgo best = new EnergyAlgo(null, -1000000000);

        /*if (optiEnergy && energy < 0){
            return best;
        }*/

        int delta = 0;

        Square lastSquare = historique.get(historique.size()-1);

        switch (lastSquare.getSquareType()){
            case 'A':
                EnergyAlgo end = new EnergyAlgo(historique, energy-1);
                return end;
            case 'O':
                return best;
            case 'B':
                delta = 9;
                break;
            case 'D':
                break;
            default:
                if (lastSquare.getWasBonnus()){
                    delta = 9;
                }else{
                    delta = -1;
                }
        }

        if (lastSquare.getX()+1 < gridWidth){
            //on peut aller vers la droite
            Square toTest = squareList.get(lastSquare.getY()*gridWidth+lastSquare.getX()+1);

            Boolean isIn = false;

            for (Square square : historique) {
                if (square.getId() == toTest.getId()){
                    isIn = true;
                }
            }
            if (!isIn){
                List<Square> tmphist = historique.stream().collect(Collectors.toList());
                tmphist.add(toTest);
                EnergyAlgo tmp = this.recursive(tmphist, squareList, energy + delta, gridWidth, gridHeight, optiEnergy);

                if (tmp.getStamina() > best.getStamina()){
                    best.setStamina(tmp.getStamina());
                    best.setHistory(tmp.getHist());
                }
            }
        }
        if (lastSquare.getX()-1 >= 0){
            //on peut aller vers la gauche
            Square toTest = squareList.get(lastSquare.getY()*gridWidth+lastSquare.getX()-1);

            Boolean isIn = false;

            for (Square square : historique) {
                if (square.getId() == toTest.getId()){
                    isIn = true;
                }
            }
            if (!isIn){
                List<Square> tmphist = historique.stream().collect(Collectors.toList());
                tmphist.add(toTest);
                EnergyAlgo tmp = this.recursive(tmphist, squareList, energy + delta, gridWidth, gridHeight, optiEnergy);

                if (tmp.getStamina() > best.getStamina()){
                    best.setStamina(tmp.getStamina());
                    best.setHistory(tmp.getHist());
                }
            }
        }
        if (lastSquare.getY()+1 < gridHeight){
            //on peut aller vers le bas
            Square toTest = squareList.get((lastSquare.getY()+1)*gridWidth+lastSquare.getX());
            
            Boolean isIn = false;

            for (Square square : historique) {
                if (square.getId() == toTest.getId()){
                    isIn = true;
                }
            }
            if (!isIn){
                List<Square> tmphist = historique.stream().collect(Collectors.toList());
                tmphist.add(toTest);
                EnergyAlgo tmp = this.recursive(tmphist, squareList, energy + delta, gridWidth, gridHeight, optiEnergy);

                if (tmp.getStamina() > best.getStamina()){
                    best.setStamina(tmp.getStamina());
                    best.setHistory(tmp.getHist());
                }
            }
        }
        if (lastSquare.getY()-1 >= 0){
            //on peut aller vers le haut
            Square toTest = squareList.get((lastSquare.getY()-1)*gridWidth+lastSquare.getX());
            
            Boolean isIn = false;

            for (Square square : historique) {
                if (square.getId() == toTest.getId()){
                    isIn = true;
                }
            }
            if (!isIn){
                List<Square> tmphist = historique.stream().collect(Collectors.toList());
                tmphist.add(toTest);
                EnergyAlgo tmp = this.recursive(tmphist, squareList, energy + delta, gridWidth, gridHeight, optiEnergy);

                if (tmp.stamina > best.getStamina()){
                    best.setStamina(tmp.stamina);
                    best.setHistory(tmp.history);
                }
            }
        }

        return best;
    }

    public void setStamina(int n){
        this.stamina = n;
    }

    public void setHistory(List<Square> hist){
        this.history = hist;
    }

    public int getStamina(){
        return this.stamina;
    }

    public List<Square> getHist(){
        return this.history;
    }
}
