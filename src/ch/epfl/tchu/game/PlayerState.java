package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente l'état complet d'un joueur.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    
    /**
     * Construit l'état d'un joueur possédant les billets, cartes et routes donnés.
     * 
     * @param tickets
     *          billets du joueur.
     * @param cards
     *          cartes du joueur.
     * @param routes
     *          routes du joueur.
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        
    	super(tickets.size(), cards.size(), routes);  // crée NullPointerException si un argument est null.
        
        this.tickets = tickets;
        this.cards = cards;        
    }
    
	/**
     * Retourne l'état initial d'un joueur auquel les cartes initiales données ont été distribuées.
     * 
     * @param initialCards
     *          cartes initiales du joueur.
     * @return l'état initial d'un joueur auquel les cartes initiales données ont été distribuées.
     * @exception IllegalArgumentException
     *          si le nombre de cartes initiales ne vaut pas 4.
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        return new PlayerState(SortedBag.of(), initialCards, new ArrayList<Route>());
    }
    
    /**
     * Retourne les billets du joueur.
     * 
     * @return les billets du joueur.
     */
    public SortedBag<Ticket> tickets() {
        return tickets;
    }
    
    /**
     * Retourne un état identique au récepteur, si ce n'est que le joueur possède en plus les billets donnés.
     * 
     * @param newTickets
     *          billets en plus.
     * @return un état identique au récepteur, si ce n'est que le joueur possède en plus les billets donnés.
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        return new PlayerState(tickets().union(newTickets), cards(), routes());
    }
    
    /**
     * Retourne les cartes wagon/locomotive du joueur.
     * 
     * @return les cartes wagon/locomotive du joueur.
     */
    public SortedBag<Card> cards() {
        return cards;
    }
    
    /**
     * Retourne un état identique au récepteur, si ce n'est que le joueur possède en plus la carte donnée.
     * 
     * @param card
     *          carte en plus.
     * @return un état identique au récepteur, si ce n'est que le joueur possède en plus la carte donnée.
     */
    public PlayerState withAddedCard(Card card) {        
        return withAddedCards(SortedBag.of(card));
    }
    
    /**
     * Retourne un état identique au récepteur, si ce n'est que le joueur possède en plus les cartes données.
     * 
     * @param additionalCards
     *          cartes en plus.
     * @return un état identique au récepteur, si ce n'est que le joueur possède en plus les cartes données.
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards) {
        return new PlayerState(tickets(), cards().union(additionalCards), routes());
    }
    
    /**
     * Retourne vrai ssi le joueur peut s'emparer de la route donnée, c-à-d s'il lui reste assez de wagons et s'il possède les cartes nécessaires.
     * 
     * @param route
     *          route dont on cherche à savoir si le joueur peut s'en emparer.
     * @return vrai ssi le joueur peut s'emparer de la route donnée, c-à-d s'il lui reste assez de wagons et s'il possède les cartes nécessaires.
     */
    public boolean canClaimRoute(Route route) {
    	
        return carCount() >= route.length() && possibleClaimCards(route).size() > 0;
    }
    
    /**
     * Retourne la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour prendre possession de la route donnée.
     * 
     * @param route
     *          route dont le joueur veut s'emparer.
     * @return la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour prendre possession de la route donnée.
     * @exception IllegalArgumentException
     *          si le joueur n'a pas assez de wagons pour s'emparer de la route.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
	
    	Preconditions.checkArgument(route != null);
    	Preconditions.checkArgument(carCount() >= route.length());
    	
    	List<SortedBag<Card>>  theList = new ArrayList<SortedBag<Card>>();
    	
    	List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();

		for (SortedBag<Card> possibleCards: possibleClaimCards) {			
			if (possibleCards.difference(cards).size() == 0)
				theList.add(possibleCards);
		}
		
		return theList;
    }
    
    /**
     * Retourne la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour s'emparer d'un tunnel,
     * trié par ordre croissant du nombre de cartes locomotives, sachant qu'il a initialement posé les cartes initialCards,
     * que les 3 cartes tirées du sommet de la pioche sont drawnCards,
     * et que ces dernières forcent le joueur à poser encore additionalCardsCount cartes.
     * 
     * @param additionalCardsCount
     *          nombre de cartes additionnelles que le joueur doit encore poser.
     * @param initialCards
     *          cartes initialement posées.
     * @param drawnCards
     *          trois cartes tirées du sommet de la pioche.
     * @return la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour s'emparer d'un tunnel.
     * @exception IllegalArgumentException
     *          si le nombre de cartes additionnelles n'est pas compris entre 1 et 3 (inclus),
     *          si l'ensemble des cartes initiales est vide ou contient plus de 2 types de cartes différents,
     *          ou si l'ensemble des cartes tirées ne contient pas exactement 3 cartes.
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards) {

        Preconditions.checkArgument(additionalCardsCount >= 1 && additionalCardsCount <= Constants.ADDITIONAL_TUNNEL_CARDS);        
        Preconditions.checkArgument(initialCards != null);
        Preconditions.checkArgument(initialCards.size() > 0);               
        Preconditions.checkArgument(initialCards.toSet().size() <= 2);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);

        List<SortedBag<Card>> theList = new ArrayList<SortedBag<Card>>();
        
        SortedBag.Builder<Card> builder = new SortedBag.Builder<Card>();
        for (Card card: initialCards.toSet()) {
            builder.add(cards.countOf(card), card);         
        }
        
        if (initialCards.countOf(Card.LOCOMOTIVE) == 0)
            builder.add(cards.countOf(Card.LOCOMOTIVE), Card.LOCOMOTIVE);           
            
        SortedBag<Card> possibleCards = builder.build();
            
        possibleCards = possibleCards.difference(initialCards);
                
        if (possibleCards.size() == 0)
            return theList;                     // Aucune carte correspondante dans la main du joueur.

        if (additionalCardsCount > possibleCards.size())
            return theList; 

        if (possibleCards.subsetsOfSize(additionalCardsCount).size() == 0)
            return theList;                     // Pas assez de cartes désirées dans la main du joueur.
        
        theList = new ArrayList<SortedBag<Card>>(possibleCards.subsetsOfSize(additionalCardsCount));
        theList.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
        
        return theList;
    }
    
    /**
     * Retourne un état identique au récepteur, si ce n'est que le joueur s'est de plus emparé de la route donnée au moyen des cartes données.
     * 
     * @param route
     *          route dont le joueur s'est emparé.
     * @param claimCards
     *          cartes ayant permis au joueur de s'emparer de la route donnée.
     * @return un état identique au récepteur, si ce n'est que le joueur s'est de plus emparé de la route donnée au moyen des cartes données.
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
    	
    	// To do: a l'etape 7 d'apres @509
    	// Lever une exception si l'utilisateur n'est pas en possession de claimCards.
    	//
    	List<Route> newRoutes = new ArrayList<Route>(routes());
    	newRoutes.add(route);
    	
    	return new PlayerState(tickets(), cards.difference(claimCards), newRoutes);
    }
    
    /**
     * Retourne le nombre de points — éventuellement négatif — obtenus par le joueur grâce à ses billets.
     * 
     * @return le nombre de points obtenus par le joueur grâce à ses billets.
     */
    public int ticketPoints() {
        
        // Etapes suivies telles que expliquees dans @744.  Voir aussi @791.
    	
    	// Etape 1: Determiner la valeur max to station.id() des stations
    	// dans la route du joueur.
    	//
    	int maxStationID = 0;
    	for (Route route: routes()) {    		
    		maxStationID = Math.max(maxStationID, route.station1().id());
    		maxStationID = Math.max(maxStationID, route.station2().id());
    	}

    	// Etape 2: créer une StationPartition en utilisant le StationPartition.Builder.
    	//    	
    	StationPartition.Builder spBuilder = new StationPartition.Builder(maxStationID + 1);

    	for (Route route: routes()) {
    		spBuilder.connect(route.station1(), route.station2());    		
    	}
    	    	
    	StationPartition stationPartition = spBuilder.build();
    	        
    	// Etape 3: calculer la valeur des tickets en utilisant StationPartition
    	// pour la connectivite'.
    	//    	        
        int tcktPoints = 0;
        for (Ticket ticket : tickets()) {
            
            tcktPoints += ticket.points(stationPartition);
        }
        
        return tcktPoints;
    }
    
    /**
     * Retourne la totalité des points obtenus par le joueur à la fin de la partie.
     * 
     * @return la totalité des points obtenus par le joueur à la fin de la partie.
     */
    public int finalPoints() {
        return claimPoints() + ticketPoints();
    }
}