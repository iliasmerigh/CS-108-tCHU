package ch.epfl.tchu.game;

import java.util.List;
import java.util.Map;

import ch.epfl.tchu.SortedBag;

/**
 * Représente un joueur de tCHu.  Les méthodes de cette interface sont destinées à être 
 * appelées à différents moments de la partie, soit pour communiquer certaines informations 
 * concernant son déroulement au joueur, soit pour obtenir certaines informations de ce dernier.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
interface Player {
	
	/**
	 * Représente les trois types d'actions qu'un joueur de tCHu peut effectuer durant un tour.
	 * 
     * @author Ilias Marwane Merigh (330316)
	 */
	public enum TurnKind {

		DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE;
			
		public static final List<TurnKind> ALL = List.of(TurnKind.values());
	}

	/**
	 * Appelée au début de la partie pour communiquer au joueur sa propre identité ownId, ainsi 
	 * que les noms des différents joueurs, le sien inclus, qui se trouvent dans playerNames.
	 */
	public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

	/**
	 * Appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie ; cette information est donnée sous la forme 
	 * d'une chaîne de caractères, généralement produite par la classe Info définie à l'étape 3,
	 */
	public abstract void receiveInfo(String info);

	/**
	 * Appelée chaque fois que l'état du jeu a changé, pour informer le joueur de la composante publique de ce nouvel état, newState, 
	 * ainsi que de son propre état, ownState.
	 */
	public abstract void updateState(PublicGameState newState, PlayerState ownState);

	/**
	 * Appelée au début de la partie pour communiquer au joueur les cinq billets qui lui ont été distribués.
	 */
	public abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);

	/**
     * Appelée au début de la partie pour demander au joueur lesquels des billets qu'on lui a 
     * distribué initialement (via la méthode précédente) il garde.
     * 
     * @return les billets distribués initialement au joueur.
     */
	public abstract SortedBag<Ticket> chooseInitialTickets();

	/**
	 * Appelée au début du tour d'un joueur, pour savoir quel type d'action il désire effectuer durant ce tour.
	 * 
	 * @return le type d'action que le joueur désire effectuer.
	 */
	public abstract TurnKind nextTurn();

	/**
	 * Appelée lorsque le joueur a décidé de tirer des billets supplémentaires en cours de partie,
	 * afin de lui communiquer les billets tirés et de savoir lesquels il garde.
	 * 
	 * @return les billets tirés.
	 */
	public abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);
	
	/**
	 * Appelée lorsque le joueur a décidé de tirer des cartes wagon/locomotive, afin de savoir d'où il désire les tirer : 
	 * d'un des emplacements contenant une carte face visible — auquel cas la valeur retourne est comprise entre 0 et 4 inclus —, 
	 * ou de la pioche — auquel cas la valeur retournée vaut Constants.DECK_SLOT (c-à-d -1).
	 * 
	 * @return l'index de la où le joueur désire tirer les cartes.
	 */
	public abstract int drawSlot();
	
	/**
	 * Appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir de quelle route il s'agit.
	 * 
	 * @return la route dont le joueur décide de s'emparer.
	 */
	public abstract Route claimedRoute();
	
	/**
	 * Appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir quelle(s) carte(s) 
	 * il désire initialement utiliser pour cela.
	 * 
	 * @return les cartes qu'il désire utiliser.
	 */
	public abstract SortedBag<Card> initialClaimCards();
	
	/**
	 * Appelée lorsque le joueur a décidé de tenter de s'emparer d'un tunnel et que des cartes 
	 * additionnelles sont nécessaires, afin de savoir quelle(s) carte(s) il désire utiliser pour cela, 
	 * les possibilités lui étant passées en argument ; si le multiensemble retourné est 
	 * vide, cela signifie que le joueur ne désire pas (ou ne peut pas) choisir l'une de ces possibilités.
	 * 
	 * @return les cartes qu'il désire utiliser.
	 */
	public abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
