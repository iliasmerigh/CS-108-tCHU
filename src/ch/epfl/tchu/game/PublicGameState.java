package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * Représente la partie publique de l'état d'une partie de tCHu.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public class PublicGameState {

	private final int ticketsCount;
	private final PublicCardState cardState;
	private final PlayerId currentPlayerId;
	private final Map<PlayerId, PublicPlayerState> playerState;
	private final PlayerId lastPlayer;
	
	/**
	 * Construit la partie publique de l'état d'une partie de tCHu dans laquelle la pioche de billets a une taille de ticketsCount,
	 * l'état public des cartes wagon/locomotive est cardState, le joueur courant est currentPlayerId,
	 * l'état public des joueurs est contenu dans playerState,
	 * et l'identité du dernier joueur est lastPlayer (qui peut être null si cette identité est encore inconnue).
	 * 
	 * @param ticketsCount
	 *         taille de la pioche de billets.
	 * @param cardState
	 *         état public des cartes wagon/locomotive.
	 * @param currentPlayerId
	 *         identité du joueur courant.
	 * @param playerState
	 *         état public des joueurs.
	 * @param lastPlayer
	 *         identité du dernier joueur.
	 * @exception IllegalArgumentException
	 *         si la taille de la pioche est strictement négative ou si playerState ne contient pas exactement deux paires clef/valeur.
	 * 
	 */
	public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {

	    Preconditions.checkArgument(ticketsCount >= 0);
        Preconditions.checkArgument(playerState.size() == PlayerId.COUNT);
	    
		this.ticketsCount = ticketsCount;
		this.cardState = Objects.requireNonNull(cardState);
		this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
		this.playerState = Objects.requireNonNull(playerState);
		this.lastPlayer = lastPlayer;
	}

	/**
     * Retourne la taille de la pioche de billets.
     * 
     * @return la taille de la pioche de billets.
     */
    public int ticketsCount() {
        return ticketsCount;
    }
	
    /**
     * Retourne vrai ssi il est possible de tirer des billets, c-à-d si la pioche n'est pas vide.
     * 
     * @return vrai ssi il est possible de tirer des billets, c-à-d si la pioche n'est pas vide.
     */
    public boolean canDrawTickets() {
    	return (ticketsCount > 0);
    };

    /**
     * Retourne la partie publique de l'état des cartes wagon/locomotive.
     * 
     * @return la partie publique de l'état des cartes wagon/locomotive.
     */
    public PublicCardState cardState() {
    	return cardState;
    }
    
    /**
     * Retourne vrai ssi il est possible de tirer des cartes, c-à-d 
     * si la pioche et la défausse contiennent entre elles au moins 5 cartes
     * 
     * @return  vrai ssi il est possible de tirer des cartes
     */    
    public boolean canDrawCards() {
    	return ((cardState.deckSize() + cardState.discardsSize()) >= Constants.ADDITIONAL_TUNNEL_CARDS + Constants.DISCARDABLE_TICKETS_COUNT);   
    }

    /**
     * Retourne l'identité du joueur actuel.
     * 
     * @return l'identité du joueur actuel.
     */    
    public PlayerId currentPlayerId() {
    	return currentPlayerId;
    }
    
    /**
     * Retourne la partie publique de l'état du joueur d'identité donnée.
     * 
     * @return la partie publique de l'état du joueur d'identité donnée.
     */        
    public PublicPlayerState playerState(PlayerId playerId) {
    	return playerState.get(playerId);
    }

    /**
     * Retourne la partie publique de l'état du joueur courant.
     * 
     * @return la partie publique de l'état du joueur courant.
     */        
    public PublicPlayerState currentPlayerState() {
    	return playerState(currentPlayerId);
    }
    
    /**
     * Retourne la totalité des routes dont l'un ou l'autre des joueurs s'est emparé.
     * 
     * @return la totalité des routes dont l'un ou l'autre des joueurs s'est emparé.    
     */            
    public List<Route> claimedRoutes() {
    	
    	List<Route> list1 = new ArrayList<Route>(playerState(currentPlayerId).routes());
    	List<Route> list2 = playerState(currentPlayerId.next()).routes();
    	list1.addAll(list2);
    	return (list1);
    }
    
    /**
     * Retourne l'identité du dernier joueur, ou null si elle n'est pas encore connue car le dernier tour n'a pas commencé.
     * 
     * @return l'identité du dernier joueur, ou null si elle n'est pas encore connue car le dernier tour n'a pas commencé. 
     */                
    public PlayerId lastPlayer() {
    	return lastPlayer;
    }
}
