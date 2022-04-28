package game4j;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ReplayViewer {
    private Game game;
    public Player player;
    public List<Square> historyList;
    public float squareLength;

    public int speed ; //toutes les secondes par défaut

    private int idFrame;

    public ReplayViewer(String fileName, AnchorPane root, Main main, String speedTitle){
        //fonction pour charger une partie qui a été sauvegardée
        switch (speedTitle){
            case "Slow":
                this.speed = 2000;
                break;
            case "Quick":
                this.speed = 500;
                break;
            default:
                this.speed = 1000; // 1sec par défaut
        }
        
        this.game = new Game(root);
        game.loadGame("ressources/replay/" + fileName);
        this.squareLength = game.getSquareWidth();
        
        Button btnMenu = new Button();
        btnMenu.setText("Retourner au menu");
        AnchorPane.setRightAnchor(btnMenu, 20.0);
        AnchorPane.setBottomAnchor(btnMenu, 10.0);

        btnMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                main.reset();
            }
        });

        root.getChildren().add(btnMenu);

        this.player = this.game.getPlayer();
        this.player.setEnergy(this.game.getStartEnergy());
        this.historyList = player.getHistorySquare();

        this.player.setX(this.historyList.get(0).getX());
        this.player.setY(this.historyList.get(0).getY());
        this.game.resetInterface();

        this.updatePlayer();

        this.idFrame = 1;
    }

    public void updatePlayer(){
        TranslateTransition translate = new TranslateTransition(Duration.millis(this.speed/2), this.player.getRendu());
        translate.setToX(this.player.getX()*this.squareLength);
        translate.setToY(this.player.getY()*this.squareLength);
        translate.play();
    }

    public void play(){
        if (this.idFrame != this.historyList.size()){
            this.player.setX(this.historyList.get(this.idFrame).getX());
            this.player.setY(this.historyList.get(this.idFrame).getY());

            

            switch (this.historyList.get(this.idFrame).getSquareType()){
                case 'O':
                    this.player.setEnergy(this.player.getEnergy()-10);
                    break;
                case 'B':
                    this.player.setEnergy(this.player.getEnergy()+9);
                    break;
                default:
                    this.player.setEnergy(this.player.getEnergy()-1);
            }

            this.idFrame += 1;
            
            Timeline t = new Timeline(
                new KeyFrame(Duration.millis(this.speed), e -> {
                    this.game.updateStaminaLabel(this.player.getEnergy());
                    this.updatePlayer();
                    play();})
            );
            t.playFromStart();
        }
        
    }
}
