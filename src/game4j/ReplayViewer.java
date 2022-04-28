package game4j;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ReplayViewer {
    private Game game;

    public ReplayViewer(String fileName, AnchorPane root, Main main){
        //fonction pour charger une partie qui a été sauvegardée
        
        this.game = new Game(root);
        game.loadGame("ressources/replay/" + fileName);
        
        Button btnMenu = new Button();
        btnMenu.setText("Retourner au menu");
        AnchorPane.setLeftAnchor(btnMenu, 20.0);
        AnchorPane.setTopAnchor(btnMenu, 10.0);

        btnMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                main.reset();
            }
        });

        this.game.resetInterface();        
    }

    public void play(){

    }
}
