package game4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sauvegarde implements Serializable{
    private List<Integer> listX = new ArrayList<Integer>();
    private List<Integer> listY = new ArrayList<Integer>();
    private float width;
    private int gridWidth;
    private int gridHeight;
    private List<Boolean> wasBonusList = new ArrayList<Boolean>();
    private List<Character> squareTypeList = new ArrayList<Character>();
    private List<int[]> distances = new ArrayList<int[]>(); //initialiser le tableau de int avant de l'attribuer

    private int IdStart;
    private int IdFinish;
    private int startEnergy;

    private int energy;
    private int playerX;
    private int playerY;
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

        this.IdStart = game.getStart().getId();
        this.IdFinish = game.getFinish().getId();
        this.startEnergy = game.getStartEnergy();

        this.energy = player.getEnergy();
        this.playerX = player.getX();
        this.playerY = player.getY();
        this.EarnedEnergy = player.getEarnedEnergy();
        this.LostEnergy = player.getLostEnergy();
        this.nbReturn = player.getnbReturn();
        for (Square square : player.getHistorySquare()) {
            this.historiqueSquareId.add(square.getId());
        }
    }

    public void load(Game game){
        game.setSquareLength(this.width);
        game.setGrid(this.gridHeight, this.gridWidth);
        
        List<Square> squareList = new ArrayList<Square>();
        for (int i = 0; i < this.listX.size(); i++){
            int x =this.listX.get(i);
            int y = this.listY.get(i);
            squareList.add(new Square(x, y, this.width, y*this.gridWidth+x, gridWidth, this.squareTypeList.get(i), this.distances.get(i)));
        }
        game.setSquareList(squareList);
        game.setStart(this.IdStart);
        game.setFinish(this.IdFinish);
        game.setStartEnergy(this.startEnergy);

        Player player = new Player(this.startEnergy, game.getStart().getX(), game.getStart().getY(), this.width);
        player.setX(this.playerX);
        player.setY(this.playerY);
        player.setEnergy(this.energy);
        player.setEarnedEnergy(this.EarnedEnergy);
        player.setLostEnergy(this.LostEnergy);
        player.setnbReturn(this.nbReturn);

        List<Square> historique = new ArrayList<Square>();
        for (int id : this.historiqueSquareId) {
            for (Square square : squareList) {
                if (square.getId() == id){
                    historique.add(square);
                }
            }
        }

        player.setHistoriqueSquare(historique);

        game.setPlayer(player);
    }
}
