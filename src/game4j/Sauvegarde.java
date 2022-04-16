package game4j;

import java.util.ArrayList;
import java.util.List;

public class Sauvegarde {
    private List<Integer> listX = new ArrayList<Integer>();
    private List<Integer> listY = new ArrayList<Integer>();
    private float width;
    private int gridWidth;
    private int gridHeight;
    private List<Boolean> wasBonusList = new ArrayList<Boolean>();
    private List<Character> squareTypeList = new ArrayList<Character>();
    private List<int[]> distances = new ArrayList<int[]>(); //initialiser le tableau de int avant de l'attribuer

    private Square start;
    private Square finish;
    private int startEnergy;
    private Boolean isFinished;

    private int energy;
    private int EarnedEnergy;
    private int LostEnergy;
    private int nbReturn;
    private List<Integer> historiqueSquareId = new ArrayList<Integer>();

    public Sauvegarde(Game game, List<Square> listesquare, Player player){
        this.width = game.getSquareWidth();
        this.gridHeight = game.getGridHeight();
        this.gridWidth = game.getGridWidth();

        for (Square square : listesquare) {
            listX.add(square.getX());
            listY.add(square.getY());
            wasBonusList.add(square.getWasBonnus());
            squareTypeList.add(square.getSquareType());
            distances.add(square.getDistanceList());
        }
    }
}
