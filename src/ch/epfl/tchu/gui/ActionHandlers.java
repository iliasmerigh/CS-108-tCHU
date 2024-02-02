package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * Contient cinq interfaces fonctionnelles imbriquées représentant différents gestionnaires d'actions.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public interface ActionHandlers {

    /**
     * Gestionnaire de l'action : tirer des billets.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public abstract interface DrawTicketsHandler {
        
        /**
         * Appelée lorsque le joueur désire tirer des billets.
         */
        public abstract void onDrawTickets();
    }
    
    /**
     * Gestionnaire de l'action : tirer une carte.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public abstract interface DrawCardHandler {
        
        /**
         * Appelée lorsque le joueur désire tirer une carte de l'emplacement donné.
         * 
         * @param index
         *          index donné (0 à 4, ou -1 pour la pioche).
         */
        public abstract void onDrawCard(int index);
    }
    
    /**
     * Gestionnaire de l'action : s'emparer d'une route.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public abstract interface ClaimRouteHandler {
        
        /**
         * Appelée lorsque le joueur désire s'emparer de la route donnée au moyen des cartes données.
         * 
         * @param route
         *          route dont le joueur veut s'emparer.
         * @param cards
         *          cartes avec lesquels il veut s'emparer de la route.
         */
        public abstract void onClaimRoute(Route route, SortedBag<Card> cards);
    }
    
    /**
     * Gestionnaire de l'action : garder des billets suite à un tirage de billet.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public abstract interface ChooseTicketsHandler {
        
        /**
         * Appelée lorsque le joueur a choisi de garder les billets donnés suite à un tirage de billets.
         * 
         * @param tickets
         *          billets que le joueur a choisi de garder.
         */
        public abstract void onChooseTickets(SortedBag<Ticket> tickets);
    }

    /**
     * Gestionnaire de l'action : utilisation de cartes pour posséder une route.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public abstract interface ChooseCardsHandler {
    
        /**
         * Appelée lorsque le joueur a choisi d'utiliser les cartes données comme cartes initiales ou
         * additionnelles lors de la prise de possession d'une route.
         * S'il s'agit de cartes additionnelles, alors le multiensemble peut être vide, ce qui signifie que le joueur renonce à s'emparer du tunnel.
         * 
         * @param cards
         *          cartes avec lesquels le joueur veut s'emparer de la route.
         */
        public abstract void onChooseCards(SortedBag<Card> cards);
    }
}
