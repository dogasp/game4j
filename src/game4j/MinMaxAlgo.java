package game4j;

import java.util.ArrayList;
import java.util.List;

public class MinMaxAlgo {
    private Square node;
    private int stamina;
    private int gridWidth;
    private int gridHeight;

    public MinMaxAlgo(Square square, int width, int height){
        this.node = square;
        this.stamina = 10000000;
        this.gridWidth = width;
        this.gridHeight = height;
    }

    public void findBestPathEnergy(List<Square> squareList){

        List<Square> result = new ArrayList<Square>();
        int n = this.recursive(result, squareList);

        System.out.print("Chemin le plus optimal niveau energie: ");
        for (Square square : result) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println("\nCela revient à utiliser " + n + " energies");
    }

    private int recursive(List<Square> historique, List<Square> squareList){
        if (this.node.getX()+1 < this.gridWidth){
            //on peut aller vers la droite
            Square totest = squareList.get(this.node.getY()*this.gridWidth+this.node.getX());
            MinMaxAlgo RightArm = new MinMaxAlgo(totest, this.gridWidth, this.gridHeight);
            int n = RightArm.recursive(, squareList)
        }

        return 0;
    }
}
