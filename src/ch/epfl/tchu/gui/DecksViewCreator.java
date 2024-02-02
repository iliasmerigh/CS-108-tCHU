package ch.epfl.tchu.gui;

import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;

/**
 * @author Ilias Marwane Merigh (330316)
 */
class DecksViewCreator {
    
    private DecksViewCreator() {}

    /**
     * Prend en argument l'état du jeu observable et retourne la vue de la main.
     * 
     * @param observableGameState
     *          état du jeu observable.
     */
    void createHandView(ObservableGameState observableGameState) {
        
    }

    /**
     * Prend en arguments l'état de jeu observable et deux propriétés contenant chacune un gestionnaire d'action : 
     * la première contient celui gérant le tirage de billets, la seconde contient celui gérant le tirage de cartes.
     * 
     * @param observableGameState
     *          état du jeu observable.
     * @param drawTicketsHandler
     *          gestionaire d'action : tirage de billets.
     * @param drawCardHandler
     *          gestionaire d'action : tirage de cartes.
     */
    void createCardsView(ObservableGameState observableGameState, DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler) {
        
    }
}
