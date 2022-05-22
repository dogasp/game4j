package game4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    private Square start;
    private Square finish;
    private int startEnergy;
    private List<Square> ListSquare;

    private int width;
    private int height;


    public Boolean generate(String difficulty, int width, int height, int obstacleAmount, int bonusAmount, boolean energy){
        this.width = width;
        this.height = height;

        Random r = new Random();
        
        //20 essais pour trouver une configuration qui marche
        for (int i = 0; i < 20; i++){
            System.out.println("Époche: " + i);
            this.ListSquare = new ArrayList<Square>();
            //création de chaque Square
            for (int j = 0; j < width*height; j ++){
                int dist[] = {r.nextInt(101), r.nextInt(101), r.nextInt(101), r.nextInt(101)};
                ListSquare.add(new Square(j%width, j/width, Main.WindowHeight/width, j, 'V', dist));
            }
            this.start = this.ListSquare.get(0);
            this.start.setType('D');

            this.finish = this.ListSquare.get(width*height-1);
            this.finish.setType('A');

            int obstacleCount = 0;
            while (obstacleCount < obstacleAmount) {
                int id = r.nextInt(width*height);
                if (this.ListSquare.get(id).getSquareType() == 'V'){
                    this.ListSquare.get(id).setType('O');
                    obstacleCount += 1;
                }
            }

            int bonnusCount = 0;
            while (bonnusCount < bonusAmount){
                int id = r.nextInt(width*height);
                if (this.ListSquare.get(id).getSquareType() == 'V'){
                    this.ListSquare.get(id).setType('B');
                    bonnusCount += 1;
                }
            }

            this.startEnergy = r.nextInt(width);

            //boucler pour trouver stamina opti
            DijkstraAlgo path = new DijkstraAlgo(this.ListSquare, this.width, this.height).optimiserDistance(this.start, this.finish, true);
            if (path.getValue() > -1){ //si le chemin est possible
                int minStamina = getMinStamina(path.getPath());
                //la difficulté définit la marge en energie qu'on a pour résoudre le problème
                if (difficulty == "Easy"){
                    this.startEnergy = (int)(minStamina*1.5);
                }
                else if (difficulty == "Medium"){
                    this.startEnergy = (int)(minStamina*1.2);
                }
                else{
                    this.startEnergy = minStamina;
                }
                return true;
            }

        }
        return false;
    }

    private int getMinStamina(List<Square> hist){
        //calcul de l'énergie minimale pour que le parcours soit réalisable
        int accumulated = 0;
        int needed = 0;
        for (Square square : hist) {
            if (square.getSquareType() != 'D'){
                if (accumulated > 0){
                    accumulated -= 1;
                } else{
                    needed += 1;
                }
                if (square.getSquareType() == 'B'){

                    accumulated += 10;
                }
            }
        }

        return needed;
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public Square getStart(){
        return this.start;
    }
    public Square getFinish(){
        return this.finish;
    }
    public List<Square> getSquareList(){
        return this.ListSquare;
    }
    public int getStartEnergy(){
        return this.startEnergy;
    }
}
