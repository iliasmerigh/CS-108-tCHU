package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente l'état d'une partie de tCHu.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class GameState extends PublicGameState {
	
	private final Deck<Ticket> gameTickets;
	private final CardState completeCardState;
	private final Map<PlayerId, PlayerState> completePlayerState;
	private PlayerId gameLastPlayer;

	private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer) {
						
		super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
		
		Objects.requireNonNull(tickets);
		Objects.requireNonNull(cardState);
		
		this.gameTickets = tickets;
		this.completePlayerState  = playerState;		
		this.completeCardState  = cardState;		
		this.gameLastPlayer  = lastPlayer;
	}
	
	/**
	 * Construit l'état initial d'une partie de tCHu dans laquelle:
     * La pioche des billets contient les billets donnés et 
     * la pioche des cartes contient les cartes de Constants.ALL_CARDS, sans les 8 (2×4) du dessus.
     * Ces pioches sont mélangées au moyen du générateur aléatoire rng, 
     * qui est aussi utilisé pour choisir au hasard l'identité du premier joueur.
     * 
	 * @param tickets
	 *         billets donnés.
	 * @param rng
	 *         générateur aléatoire.
	 * @return l'état initial de la partie construite.
	 */
	public static GameState initial(SortedBag<Ticket> tickets, Random rng) {

		// Cards
		//
		Deck<Card> deckCards = Deck.of(Constants.ALL_CARDS, rng);	

		SortedBag<Card> player1Cards = deckCards.topCards(Constants.INITIAL_CARDS_COUNT);
	    deckCards = deckCards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
		
	    SortedBag<Card> player2Cards = deckCards.topCards(Constants.INITIAL_CARDS_COUNT);
	    deckCards = deckCards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
	    
	    CardState cardState =  CardState.of(deckCards);

		// Tickets
		//
		Deck<Ticket> deckTickets = Deck.of(tickets, rng);

		SortedBag<Ticket> player1Tickets = SortedBag.of();
		SortedBag<Ticket> player2Tickets = SortedBag.of();
		
		Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
		playerState.put(PlayerId.PLAYER_1, new PlayerState(player1Tickets, player1Cards, new ArrayList<Route>()));
		playerState.put(PlayerId.PLAYER_2, new PlayerState(player2Tickets, player2Cards, new ArrayList<Route>()));

		
		PlayerId currentPlayerId = (rng.nextInt(2) == 0)? PlayerId.PLAYER_1 : PlayerId.PLAYER_2;
		PlayerId lastPlayer = null;
		
		return new GameState(deckTickets, cardState, currentPlayerId, playerState, lastPlayer);
	}	

	/**
     * Retourne l'état du joueur d'identité donnée. 
     * 
     * @return l'état du joueur d'identité donnée.
     */           	
	public PlayerState playerState(PlayerId playerId) {
		return completePlayerState.get(playerId);
	}

	 /**
     * Retourne l'état du joueur courant 
     * 
     * @return l'état du joueur courant     
     */           	
	public PlayerState currentPlayerState() {
		return completePlayerState.get(currentPlayerId());
	}

	/**
     * Retourne les 'count' billets du sommet de la pioche
     * @param count
     *         Nombre de billets a retourner
     * @return Les billets du sommet de la pioche
     * @exception IllegalArgumentException
     *         si count n'est pas compris entre 0 et la taille de la pioche (inclus)
     */
	public SortedBag<Ticket> topTickets(int count) {		
		return gameTickets.topCards(count);
	};

	/**
     * Retourne  un état identique au récepteur, mais sans les count billets du 
     * sommet de la pioche.
     * 
     * @param count
     *         Nombre de billets a enlever
     * @return
     *         Les billets du sommet de la pioche
     * @exception IllegalArgumentException
     *         si count n'est pas compris entre 0 et la taille de la pioche (inclus)
     */
	public GameState withoutTopTickets(int count) {
		return new GameState(gameTickets.withoutTopCards(count), completeCardState, currentPlayerId(), completePlayerState, lastPlayer());	
	};

	/**
     * Retourne la carte au sommet de la pioche.
     * 
     * @return La carte du sommet de la pioche
     * @exception IllegalArgumentException
     *         si la pioche est vide
     */
	public Card  topCard () {
		
		Preconditions.checkArgument(!completeCardState.isDeckEmpty());
		return completeCardState.topDeckCard();
	};

	/**
     * Retourne  un état identique au récepteur, mais sans la carte au sommet de la pioche.
     * 
     * @return Etat identique au récepteur, mais sans la carte du sommet.
     */
	public GameState withoutTopCard() {
		return new GameState(gameTickets, completeCardState.withoutTopDeckCard(), currentPlayerId(), completePlayerState, lastPlayer());
	};

	/**
     * Retourne un état identique au récepteur mais avec les cartes données ajoutées à la défausse.
     * 
     * @param discardedCards
     *          cartes à ajouter à la défausse.
     * @return un état identique au récepteur mais avec les cartes données ajoutées à la défausse.
     */
	public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
		
		return new GameState(gameTickets, completeCardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), completePlayerState, lastPlayer());
	}
	
	/**
     * Retourne un état identique au récepteur sauf si la pioche de cartes est vide, auquel 
     * cas elle est recréée à partir de la défausse, mélangée au moyen du générateur 
     * aléatoire donné.
     * 
     * @param rng
     *          générateur de nombres aléatoires.
     * @return
     *          un état identique au récepteur sauf si la pioche de cartes est vide, auquel 
     *          cas elle est recréée à partir de la défausse, mélangée au moyen du 
     *          générateur.
     */ 
	public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
		
		if (!completeCardState.isDeckEmpty())
			return this;

		return new GameState(gameTickets, completeCardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), completePlayerState, lastPlayer());		
	}	
	
	/**
     * Retourne un état identique au récepteur mais dans lequel les billets donnés ont 
     * été ajoutés à la main du joueur donné.
     * 
     * @param playerId
     *         joueur donné.
     * @param chosenTickets
     *         billets donnés.
     * @return un état identique au récepteur mais dans lequel les billets donnés ont été ajoutés à la main du joueur donné.
     * 
     * @exception IllegalArgumentException 
     *         si le joueur en question possède déjà au moins un billet 
     */
	public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
		
		PlayerState thisPlayerState = completePlayerState.get(playerId);		
		Preconditions.checkArgument(thisPlayerState.ticketPoints() == 0);
		
		PlayerState newPlayerState = new PlayerState(chosenTickets, thisPlayerState.cards(), thisPlayerState.routes());
		
		PlayerId otherPlayerId = playerId.next();
		PlayerState otherPlayerState = completePlayerState.get(otherPlayerId);		
		
		Map<PlayerId, PlayerState> newcompletePlayerState = new EnumMap<>(PlayerId.class);
		newcompletePlayerState.put(playerId, newPlayerState);
		newcompletePlayerState.put(otherPlayerId, otherPlayerState);
				
		return new GameState(gameTickets, completeCardState, currentPlayerId(), newcompletePlayerState, lastPlayer());
		
	}

	/**
     * Retourne un état identique au récepteur, mais dans lequel le joueur courant a tiré 
     * les billets drawnTickets du sommet de la pioche, et choisi de garder ceux contenus 
     * dans chosenTicket.
     * 
     * @param drawnTickets
     *         Billets tirés par le joueur courant.
     * @param chosenTickets
     *         Billets que le joueur courant a choisi de garder.
     * @return
     *         un état identique au récepteur, mais dans lequel le joueur courant a tiré 
     *         les billets drawnTickets du sommet de la pioche, et choisi de garder ceux contenus 
     *         dans chosenTicket.
     * @exception IllegalArgumentException 
     *         si l'ensemble des billets gardés n'est pas inclus dans celui des billets tirés.
     */
	public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {

		PlayerId thisPlayer = currentPlayerId();
		PlayerId otherPlayer = currentPlayerId().next();
		
		Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

		PlayerState playerState = completePlayerState.get(thisPlayer);		
		PlayerState newPlayerState = playerState.withAddedTickets(chosenTickets);
		
		PlayerState otherPlayerState = completePlayerState.get(otherPlayer);		

		Map<PlayerId, PlayerState> newcompletePlayerState = new EnumMap<>(PlayerId.class);
		newcompletePlayerState.put(thisPlayer, newPlayerState);
		newcompletePlayerState.put(otherPlayer, otherPlayerState);
		
		// Retirer les billets de la pioche.
		//
		Deck<Ticket> newGameTickets = gameTickets.withoutTopCards(drawnTickets.size());
				
		return new GameState(newGameTickets, completeCardState, currentPlayerId(), newcompletePlayerState, lastPlayer());
	}
	
	/**
     * Retourne un état identique au récepteur si ce n'est que la carte face retournée 
     * à l'emplacement donné a été placée dans la main du joueur courant, et 
     * remplacée par celle au sommet de la pioche.
     * 
     * @param slot
     *         empalcement donné de la carte face retournée.
     * @return
     *         un état identique au récepteur si ce n'est que la carte face retournée 
     *         à l'emplacement donné a été placée dans la main du joueur courant, et 
     *         remplacée par celle au sommet de la pioche.
     * @exception IllegalArgumentException 
     *         s'il n'est pas possible de tirer des cartes, c-à-d si canDrawCards retourne faux.
     */
	public GameState withDrawnFaceUpCard(int slot) {

		Preconditions.checkArgument(canDrawCards());
		
		PlayerId thisPlayer = currentPlayerId();
		PlayerId otherPlayer = currentPlayerId().next();
		Map<PlayerId, PlayerState> newcompletePlayerState = new EnumMap<>(PlayerId.class);

		Card faceUpCard = completeCardState.faceUpCard(slot);
		CardState newcompleteCardState = completeCardState.withDrawnFaceUpCard(slot);
		
		PlayerState newPlayerState = completePlayerState.get(thisPlayer).withAddedCard(faceUpCard);
		PlayerState otherPlayerState = completePlayerState.get(otherPlayer);		
		newcompletePlayerState.put(thisPlayer, newPlayerState);
		newcompletePlayerState.put(otherPlayer, otherPlayerState);
								
		return new GameState(gameTickets, newcompleteCardState, currentPlayerId(), newcompletePlayerState, lastPlayer());

	}
	
	/**
     * Retourne un état identique au récepteur si ce n'est que la carte du sommet de la 
     * pioche a été placée dans la main du joueur courant.
     *  
     * @return
     *         un état identique au récepteur si ce n'est que la carte du sommet de la 
     *         pioche a été placée dans la main du joueur courant.
     * 
     * @exception IllegalArgumentException 
     *         s'il n'est pas possible de tirer des cartes, c-à-d si canDrawCards retourne faux,
     */
	public GameState withBlindlyDrawnCard() {

		Preconditions.checkArgument(canDrawCards());
		
		PlayerId thisPlayer = currentPlayerId();
		PlayerId otherPlayer = currentPlayerId().next();
		Map<PlayerId, PlayerState> newcompletePlayerState = new EnumMap<>(PlayerId.class);

		Card blindCard = completeCardState.topDeckCard();
		CardState newcompleteCardState = completeCardState.withoutTopDeckCard();
		
		PlayerState newPlayerState = completePlayerState.get(thisPlayer).withAddedCard(blindCard);
		PlayerState otherPlayerState = completePlayerState.get(otherPlayer);		
		newcompletePlayerState.put(thisPlayer, newPlayerState);
		newcompletePlayerState.put(otherPlayer, otherPlayerState);
								
		return new GameState(gameTickets, newcompleteCardState, currentPlayerId(), newcompletePlayerState, lastPlayer());
	}

	/**
     * Retourne un état identique au récepteur mais dans lequel le joueur courant s'est 
     * emparé de la route donnée au moyen des cartes données.
     * 
     * @param route
     *         route dont le joueur courant s'est emparé.
     * @param cards
     *         cartes ayant permis au joueur courant de s'emparer de la route.
     * @return
     *         un état identique au récepteur mais dans lequel le joueur courant s'est 
     *         emparé de la route donnée au moyen des cartes données.
     */
	public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {
		
		PlayerId thisPlayer = currentPlayerId();
		PlayerId otherPlayer = currentPlayerId().next();
		
		PlayerState playerState = completePlayerState.get(thisPlayer);
		PlayerState  newPlayerState = playerState.withClaimedRoute(route, cards);
		
		PlayerState otherPlayerState = completePlayerState.get(otherPlayer);		

		Map<PlayerId, PlayerState> newcompletePlayerState = new EnumMap<>(PlayerId.class);
		newcompletePlayerState.put(thisPlayer, newPlayerState);
		newcompletePlayerState.put(otherPlayer, otherPlayerState);
		
		GameState gameState = new GameState(gameTickets, completeCardState, currentPlayerId(), newcompletePlayerState, lastPlayer());
				
		return gameState.withMoreDiscardedCards(cards);	
	}
	
	/**
     * Retourne vrai ssi le dernier tour commence, c-à-d si l'identité du dernier joueur 
     * est actuellement inconnue mais que le joueur courant n'a plus que deux wagons 
     * ou moins ; cette méthode doit être appelée uniquement à la fin du tour d'un 
     * joueur.
     * 
     * @return vrai ssi le dernier tour commence
     */
	public boolean lastTurnBegins() {
		return ((lastPlayer() == null) && (currentPlayerState().carCount() <= 2));  // TO DO: IMM use Constants.?????
	}
	
	/**
     * Termine le tour du joueur courant, c-à-d retourne un état identique au récepteur 
     * si ce n'est que le joueur courant est celui qui suit le joueur courant actuel ; 
     * de plus, si lastTurnBegins retourne vrai, le joueur courant actuel devient 
     * le dernier joueur.   
     * 
     * @return vrai ssi le joueur courant actuel devient le dernier joueur.
     */
	public GameState forNextTurn() {
		
		if (lastTurnBegins())
			gameLastPlayer = currentPlayerId();
				
		return new GameState(gameTickets, completeCardState, currentPlayerId().next(), completePlayerState, lastPlayer());
	}
	
	/**
     * Retourne l'identité du dernier joueur, ou null si elle n'est pas encore connue car le dernier 
     * tour n'a pas commencé.
     * Note de @882_f2: "lastPlayer is not the second player to have played but the last player who 
     * will play in this game of tCHu (meaning the game is over after their turn)."
     * 
     * @return
     *         l'identité du dernier joueur, ou null si elle n'est pas encore connue car le dernier tour n'a pas commencé.
     */
	 public PlayerId lastPlayer() {
		return gameLastPlayer; 
	 }
}
