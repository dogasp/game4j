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
        
        //100 essais pour trouver une configuration qui marche
        for (int i = 0; i < 100; i++){
            this.ListSquare = new ArrayList<Square>();
            //création de chaque Square
            for (int j = 0; j < width*height; j ++){
                int dist[] = {r.nextInt(101), r.nextInt(101), r.nextInt(101), r.nextInt(101)};
                ListSquare.add(new Square(j%width, j/width, Main.WindowHeight/width, j, width, 'V', dist));
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

            //boucler pour trouver stamina opti
            EnergyAlgo path = new EnergyAlgo(null, 0).findBestPathEnergy(this.ListSquare, width, height, startEnergy, this.start, energy);
            if (path.getStamina() != -1000000000 && (energy && (path.getStamina() > 0))){ //si le chemin est possible et correspond aux attentes du joueur
                //la difficulté définit la marge en energie qu'on a pour résoudre le problème
                if (difficulty == "Easy"){
                    this.startEnergy = (int)(path.getStamina()*1.5);
                }
                else if (difficulty == "Medium"){
                    this.startEnergy = (int)(path.getStamina()*1.2);
                }
                else{
                    this.startEnergy = path.getStamina();
                }
                return true;
            }

        }
        return false;
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
