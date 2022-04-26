package game4j;

import java.util.ArrayList;
import java.util.List;

public class MinMaxAlgo {
    private Square node;
    private int stamina;
    private int gridWidth;
    private int gridHeight;

    //mauvais algorithme
    public MinMaxAlgo(Square square, int width, int height, int stamina){
        this.node = square;
        this.stamina = stamina;
        this.gridWidth = width;
        this.gridHeight = height;
    }

    public void findBestPathEnergy(List<Square> squareList){

        List<Square> result = new ArrayList<Square>();
        this.recursive(result, squareList);

        System.out.print("Chemin le plus optimal niveau energie: ");
        for (Square square : result) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println("\nCela revient à " + this.stamina + " energies");
    }

    private void recursive(List<Square> historique, List<Square> squareList){
        List<Square> besthistory = new ArrayList<Square>();

        int delta = 0;
        Boolean isBetter = false;

        switch (this.node.getSquareType()){
            case 'A':
                this.stamina = 0;
                return;
            case 'O':
                this.stamina = -1;
                return;
            case 'B':
                delta = 9;
                break;
            default:
                delta = -1;
        }

        if (this.node.getX()+1 < this.gridWidth){
            //on peut aller vers la droite
            Square totest = squareList.get(this.node.getY()*this.gridWidth+this.node.getX()+1);
            MinMaxAlgo NextNode = new MinMaxAlgo(totest, this.gridWidth, this.gridHeight, this.stamina + delta);
            List<Square> tmphist = historique;
            tmphist.add(this.node);
            NextNode.recursive(tmphist, squareList);

            if (NextNode.stamina > -1){
                this.stamina = NextNode.stamina;
                besthistory = tmphist;
                isBetter = true;
            }
        }
        if (this.node.getX()-1 >= 0){
            //on peut aller vers la gauche
            Square totest = squareList.get(this.node.getY()*this.gridWidth+this.node.getX()-1);
            MinMaxAlgo NextNode = new MinMaxAlgo(totest, this.gridWidth, this.gridHeight, this.stamina + delta);
            List<Square> tmphist = historique;
            tmphist.add(this.node);
            NextNode.recursive(tmphist, squareList);

            if (NextNode.stamina > -1){
                this.stamina = NextNode.stamina;
                besthistory = tmphist;
                isBetter = true;
            }
        }
        if (this.node.getY()+1 < this.gridHeight){
            //on peut aller vers le bas
            Square totest = squareList.get((this.node.getY()+1)*this.gridWidth+this.node.getX());
            MinMaxAlgo NextNode = new MinMaxAlgo(totest, this.gridWidth, this.gridHeight, this.stamina + delta);
            List<Square> tmphist = historique;
            tmphist.add(this.node);
            NextNode.recursive(tmphist, squareList);

            if (NextNode.stamina > -1){
                this.stamina = NextNode.stamina;
                besthistory = tmphist;
                isBetter = true;
            }
        }
        if (this.node.getY()-1 < this.gridHeight){
            //on peut aller vers le haut
            Square totest = squareList.get((this.node.getY()-1)*this.gridWidth+this.node.getX());
            MinMaxAlgo NextNode = new MinMaxAlgo(totest, this.gridWidth, this.gridHeight, this.stamina + delta);
            List<Square> tmphist = historique;
            tmphist.add(this.node);
            NextNode.recursive(tmphist, squareList);

            if (NextNode.stamina > -1){
                this.stamina = NextNode.stamina;
                besthistory = tmphist;
                isBetter = true;
            }
        }

        if (isBetter){
            System.out.println(this.stamina+" " + historique.size());
            historique = besthistory;
        }
    }
}
