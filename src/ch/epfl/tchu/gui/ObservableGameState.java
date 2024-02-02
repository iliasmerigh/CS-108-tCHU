package ch.epfl.tchu.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.javafx.collections.MappingChange.Map;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.PublicPlayerState;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Représente l'état observable d'une partie de tCHu.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public class ObservableGameState {

    private final PlayerId associatedPlayerId;
    
    // Premier groupe concerne l'état public de la partie.
    //
    private int ticketPercentage;
    private int cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final HashMap<Route, PlayerId> routesAndPlayerId;
    
    // Deuxième groupe concerne l'état public de chacun des joueurs.
    //
    private HashMap<PlayerId, Integer> eachPlayerNbOfTickets;
    private HashMap<PlayerId, Integer> eachPlayerNbOfCards;
    private HashMap<PlayerId, Integer> eachPlayerNbOfWagons;
    private HashMap<PlayerId, Integer> eachPlayerNbOfPoints;
    
    // Troisième concerne l'état privé du joueur auquel l'instance de ObservableGameState correspond.
    //
    private List<Ticket> ticketsList;
    private SortedBag<Card> nbOfEachCard;
    private HashMap<Route, Boolean> routesDisponibility;
    
    public ObservableGameState(PlayerId playerId) {
        
        associatedPlayerId = playerId;
        faceUpCards= createFaceUpCards();
    }
    
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        
        // ticketPercentage
        ticketPercentage = (newGameState.ticketsCount() / ChMap.tickets().size()) * 100;
        
        // cardPercentage
        cardPercentage = (newGameState.cardState().deckSize() / Constants.TOTAL_CARDS_COUNT) * 100;
        
        // faceUpCards
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        
        for (PlayerId playerId : PlayerId.ALL) {
            
            PublicPlayerState publicPlayerState = newGameState.playerState(playerId);
            
            // eachPlayerNbOfTickets
            eachPlayerNbOfTickets.put(playerId, publicPlayerState.ticketCount());
            
            // eachPlayerNbOfCards
            eachPlayerNbOfCards.put(playerId, publicPlayerState.cardCount());
            
            // eachPlayerNbOfWagons
            eachPlayerNbOfWagons.put(playerId, publicPlayerState.carCount());
            
            // eachPlayerNbOfPoints
            eachPlayerNbOfPoints.put(playerId, publicPlayerState.claimPoints());
        }
        
        for (Route route : ChMap.routes()) {
            
            // routesAndPlayerId
            for (PlayerId playerId : PlayerId.ALL) {
                
                if (newGameState.playerState(playerId).routes().contains(route)) {
                    
                    routesAndPlayerId.put(route, playerId);
                    break;
                }
            }
            
            // routesDisponibility
            boolean canTakeRoute = newGameState.currentPlayerId() == associatedPlayerId && 
                                    newPlayerState.canClaimRoute(route) &&
                                    !newGameState.claimedRoutes().contains(route);
            
            routesDisponibility.put(route, canTakeRoute);
        }
        
        // ticketsList
        ticketsList = newPlayerState.tickets().toList();
        
        // nbOfEachCard
        nbOfEachCard = newPlayerState.cards();
    }
    
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        
    }
    
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }
/*   
    public ReadOnlyIntegerProperty getTicketPercentage() {
        return ticketPercentage;
    }
    */
}
