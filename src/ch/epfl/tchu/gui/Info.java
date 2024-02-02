package ch.epfl.tchu.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

/**
 * Permet de générer les textes décrivant le déroulement de la partie.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Info {

    private final String playerName;
    
    /**
     * Construit un générateur de messages liés au joueur ayant le nom donné.
     * 
     * @param playerName
     *          nom du joueur.
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Retourne le nom (français) de la carte donnée, au singulier ssi la valeur absolue du second argument vaut 1.
     * 
     * @param card
     *          carte dont il faut retourner le nom.
     * @param count
     *          indicateur singulier/pluriel.
     * @return le nom (français) de la carte donnée, au singulier ssi la valeur absolue du second argument vaut 1.
     */
    public static String cardName(Card card, int count) {
        
        String plural = StringsFr.plural(count);
        
        switch (card) {
	        case BLACK: return StringsFr.BLACK_CARD + plural;
	        case VIOLET: return StringsFr.VIOLET_CARD + plural;
	        case BLUE: return StringsFr.BLUE_CARD + plural;
	        case GREEN: return StringsFr.GREEN_CARD + plural ;
	        case YELLOW: return StringsFr.YELLOW_CARD + plural;
	        case ORANGE: return StringsFr.ORANGE_CARD + plural;
	        case RED: return StringsFr.RED_CARD + plural ;
	        case WHITE: return StringsFr.WHITE_CARD + plural;
	        case LOCOMOTIVE: return StringsFr.LOCOMOTIVE_CARD + plural;
	        default: throw new IllegalArgumentException();
        }
        
    }
    
    /**
     * Retourne le message déclarant que les joueurs, dont les noms sont ceux donnés,
     * ont terminé la partie ex æqo en ayant chacun remporté les points donnés.
     * 
     * @param playerNames
     *          noms des joueurs.
     * @param points
     *          points des joueurs (même nombre de points car ils sont ex æqo).
     * @return le message déclarant que les joueurs sont ex æqo ainsi que leur nombre de points.
     */
    public static String draw(List<String> playerNames, int points) {
    	
    	ArrayList<String> safePlayerNames;
    	
    	if (playerNames == null)
    		safePlayerNames = new ArrayList<String>();    	
    	else
    		safePlayerNames = new ArrayList<String>(playerNames);
    	
    	safePlayerNames.add("UN-NAMED PLAYER");

    	safePlayerNames.add("OTHER UN-NAMED PLAYER");
    	
        return String.format(StringsFr.DRAW, String.join(StringsFr.AND_SEPARATOR, safePlayerNames.subList(0,  2)), points);
    }
    
    /**
     * Retourne le message déclarant que le joueur jouera en premier.
     * 
     * @return le message déclarant que le joueur jouera en premier.
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }
    
    /**
     * Retourne le message déclarant que le joueur a gardé le nombre de billets donné.
     * 
     * @param count
     *          nombre de billets.
     * @return le message déclarant que le joueur a gardé le nombre de billets donné.
     */
    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }
    
    /**
     * Retourne le message déclarant que le joueur peut jouer.
     * 
     * @return le message déclarant que le joueur peut jouer.
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }
    
    /**
     * Retourne le message déclarant que le joueur a tiré le nombre donné de billets.
     * 
     * @param count
     *          nombre de billets tirés.
     * @return le message déclarant que le joueur a tiré le nombre donné de billets.
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }
    
    /**
     * Retourne le message déclarant que le joueur a tiré une carte « à l'aveugle », c-à-d du sommet de la pioche.
     * 
     * @return le message déclarant que le joueur a tiré une carte « à l'aveugle », c-à-d du sommet de la pioche.
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }
    
    /**
     * Retourne le message déclarant que le joueur a tiré la carte disposée face visible donnée.
     * 
     * @param card
     *          carte face visible tirée par le joueur.
     * @return le message déclarant que le joueur a tiré la carte disposée face visible donnée.
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }
    
    /**
     * Retourne le message déclarant que le joueur s'est emparé de la route donnée au moyen des cartes données.
     * 
     * @param route
     *          route dont le joueur s'est emparé.
     * @param cards
     *          cartes qui ont permis au joueur de s'emparer de la route donnée.
     * @return le message déclarant que le joueur s'est emparé de la route donnée au moyen des cartes données.
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {        
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, computeRouteName(route), computeCardsName(cards));
    }
    
    /**
     * Retourne le message déclarant que le joueur désire s'emparer de la route en tunnel donnée en utilisant initialement les cartes données.
     * 
     * @param route
     *          route dont le joeur veut s'emparer.
     * @param initialCards
     *          cartes à utiliser pour s'emparer de la route donnée.
     * @return le message déclarant que le joueur désire s'emparer de la route en tunnel donnée en utilisant initialement les cartes données.
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, computeRouteName(route), computeCardsName(initialCards));
    }
    
    /**
     * Retourne le message déclarant que le joueur a tiré les trois cartes additionnelles données,
     * et qu'elles impliquent un coût additionel du nombre de cartes donné.
     * 
     * @param drawnCards
     *          cartes tirées par le joueur.
     * @param additionalCost
     *          coût additionel du nombre de cartes donné.
     * @return le message déclarant que le joueur a tiré les trois cartes additionnelles données,
     * et qu'elles impliquent un coût additionel du nombre de cartes donné.
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        
        if (additionalCost == 0) {
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE + StringsFr.NO_ADDITIONAL_COST, computeCardsName(drawnCards));
        
        } else {
            return String.format(StringsFr.ADDITIONAL_CARDS_ARE + StringsFr.SOME_ADDITIONAL_COST, computeCardsName(drawnCards), additionalCost, StringsFr.plural(additionalCost));
        }
    }
    
    /**
     * Retourne le message déclarant que le joueur n'a pas pu (ou voulu) s'emparer du tunnel donné.
     * 
     * @param route
     *          tunnel donné.
     * @return le message déclarant que le joueur n'a pas pu (ou voulu) s'emparer du tunnel donné.
     */
    public String didNotClaimRoute(Route route) {        
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, computeRouteName(route));
    }
    
    /**
     * Retourne le message déclarant que le joueur n'a plus que le nombre donné (et inférieur ou égale à 2) de wagons,
     * et que le dernier tour commence donc.
     * 
     * @param carCount
     *          nombre de wagons restants chez le joueur.
     * @return le message déclarant que le joueur n'a plus que le nombre donné (et inférieur ou égale à 2) de wagons,
     * et que le dernier tour commence donc.
     */
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }
    
    /**
     * Retourne le message déclarant que le joueur obtient le bonus de fin de partie grâce au chemin donné,
     * qui est le plus long, ou l'un des plus longs.
     * 
     * @param longestTrail
     *          long chemin permettant d'avoir le bonus.
     * @return le message déclarant que le joueur obtient le bonus de fin de partie.
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        
        String lgstTrail = longestTrail.station1().name() + StringsFr.EN_DASH_SEPARATOR + longestTrail.station2().name();
        
        return String.format(StringsFr.GETS_BONUS, playerName, lgstTrail);
    }
    
    /**
     * Retourne le message déclarant que le joueur remporte la partie avec le nombre de points donnés,
     * son adversaire n'en ayant obtenu que loserPoints.
     * 
     * @param points
     *          nombre de points du joueur gagnant.
     * @param loserPoints
     *          nombre de points du joueur perdant.
     * @return le message déclarant que le joueur remporte la partie avec le nombre de points donnés, ainsi que les points du joueur perdant.
     */
    public String won(int points, int loserPoints) {
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }
    
    /**
     * Retourne la représentation textuelle de la route donnée.
     * 
     * @param route
     *          route dont il faut retourner la représentation textuelle.
     * @return la représentation textuelle de la route donnée.
     */
    private static String computeRouteName(Route route) {
        return route.station1().name() + StringsFr.EN_DASH_SEPARATOR + route.station2().name();
    }
    
    /**
     * Retourne la représentation textuelle d'un ensemble de cartes.
     * 
     * @param cards
     *          ensemble de cartes dont il faut retourner la représentation textuelle.
     * @return la représentation textuelle de l'ensemble de cartes donné.
     */
    private static String computeCardsName(SortedBag<Card> cards) {
        
        String fullString = "";
        String separator = "";
        int counter = 0;
        
        if (cards == null || cards.size() < 1)
        	return "";
        
        int  cardsCount = cards.toSet().size();
        
        for (Card card : cards.toSet()) {
            
            int quantity = cards.countOf(card);
            fullString = fullString + separator + quantity + " " + cardName(card, quantity);            
            
            counter++;
            separator = (counter < cardsCount - 1) ? ", ": StringsFr.AND_SEPARATOR;
            
        }
        
        return fullString;
    }
}
