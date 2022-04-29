package game4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraAlgo {
    private int[] optilist; //valeurs de la distance à maximiser
    private int[] idList; //id pour backtrack
    private List<Integer> idDone = new ArrayList<Integer>();
    private List<Square> squarelist= new ArrayList<Square>();
    private int gridWidth;
    private int gridHeight;

    public DijkstraAlgo(List<Square> squarelist, int width, int height){
        this.squarelist = squarelist;
        this.gridWidth = width;
        this.gridHeight = height;

        int length = squarelist.size();
        this.optilist = new int[length];
        this.idList = new int[length];
        for (int i = 0; i < length; i ++){
            this.optilist[i] = 1000000;
            this.idList[i] = -1;
        }
    }

    //fonction pour trouver le chemin le plus court en distance pour atteindre l'arrivée
    public void optimiserDistance(Square start, Square end){
        this.optilist[start.getId()] = 0; //initialisation avec le départ
        this.idList[start.getId()] = -1;

        Square selected = start;
        int actualDistance = 0;

        while (selected.getSquareType() != 'A'){ //tant que l'arrivée n'est pas optimisée, on boucle
            this.idDone.add(selected.getId()); //ajout aux noeuds qui sont calculés

            int[] distanceList = selected.getDistanceList();

            //parcours des cases acessibles depuis là où on est, on part du principe que les obstacles sont forcément négatif
            //à ajouter: check si on n'est pas hors bordure

            if(selected.getY()-1 >= 0){
                int id = (selected.getY()-1)*this.gridWidth+selected.getX(); //calcul de l'id correspondant à la case visée par le déplacement
                if (this.squarelist.get(id).getSquareType() != 'O'){        // check si elle n'est pas un obstacle
                    if (actualDistance + distanceList[0] < this.optilist[id]){  //si le trajet est plus court, on l'enregistre
                        this.optilist[id] = actualDistance + distanceList[0];
                        this.idList[id] = selected.getId();
                    }
                }
            }
            if(selected.getX()+1 < this.gridWidth){
                int id = selected.getY()*this.gridWidth+selected.getX()+1;
                if (this.squarelist.get(id).getSquareType() != 'O'){
                    if (actualDistance + distanceList[1] < this.optilist[id]){
                        this.optilist[id] = actualDistance + distanceList[1];
                        this.idList[id] = selected.getId();
                    }
                }
            }
            if(selected.getY()+1 < this.gridHeight){
                int id = (selected.getY()+1)*this.gridWidth+selected.getX();
                if (this.squarelist.get(id).getSquareType() != 'O'){
                    if (actualDistance + distanceList[2] < this.optilist[id]){
                        this.optilist[id] = actualDistance + distanceList[2];
                        this.idList[id] = selected.getId();
                    }
                }
            }
            if(selected.getX()-1 >= 0){
                int id = selected.getY()*this.gridWidth+selected.getX()-1;
                if (this.squarelist.get(id).getSquareType() != 'O'){
                    if (actualDistance + distanceList[3] < this.optilist[id]){
                        this.optilist[id] = actualDistance + distanceList[3];
                        this.idList[id] = selected.getId();
                    }
                }
            }

            //selection de la prochaine node (la plus petite distance qui n'as pas été calculée)

            int minId = 0;
            int minVal = 10000;
            for (int i = 0; i < this.squarelist.size(); i++){
                if (!this.idDone.contains(i) && this.optilist[i] < minVal){
                    minVal = this.optilist[i];
                    minId = i;
                }
            }

            selected = this.squarelist.get(minId);
            actualDistance = minVal;
        }

        this.idDone.add(selected.getId()); // ajout de l'arrivée

        //rembobiner boucle infinie
        List<Square> result = new ArrayList<Square>();
        int id = selected.getId();
        result.add(selected);
        while (id != start.getId()){
            id = this.idList[id];
            result.add(this.squarelist.get(id));
        }
        Collections.reverse(result);
        
        System.out.print("Chemin le plus optimal niveau distance: ");
        for (Square square : result) {
            System.out.print(" -> [" + square.getX() + "; " + square.getY() + "]");
        }
        System.out.println("\nCela revient à parcourir " + this.optilist[selected.getId()]);
    }

}