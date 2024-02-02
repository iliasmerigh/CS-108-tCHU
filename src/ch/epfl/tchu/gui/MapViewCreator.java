package ch.epfl.tchu.gui;

import ch.epfl.tchu.gui.ActionHandlers.ChooseCardsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ch.epfl.tchu.game.Card;

import java.util.List;

import ch.epfl.tchu.SortedBag;

/**
 * @author Ilias Marwane Merigh (330316)
 */
class MapViewCreator {

    private MapViewCreator() {}
    
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);        // exemple de définition : handler.onChooseCards(options.get(0));
    }
    
    /**
     * Permet de créer la vue de la carte.
     * 
     * @param observableGameState
     *          état observable du jeu.
     * @param claimRouteHandler
     *          gestionaire d'action : s'emparer d'une route.
     * @param cardChooser
     *          selectionneur de cartes.
     */
    public void createMapView(ObservableGameState observableGameState, ObjectProperty<ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        
 /*       Pane myPane = new Pane();
        ImageView mapBackground = new ImageView();
        
        myPane.getChildren().add(mapBackground);
        */
    }
}
