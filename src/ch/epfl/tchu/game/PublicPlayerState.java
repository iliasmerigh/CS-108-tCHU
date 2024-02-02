package ch.epfl.tchu.game;

import java.util.List;

import ch.epfl.tchu.Preconditions;

/**
 * Représente la partie publique de l'état d'un joueur, à savoir le nombre de billets, de cartes et de wagons qu'il possède,
 * les routes dont il s'est emparé, et le nombre de points de construction qu'il a ainsi obtenu.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;
    
    /**
     * Construit l'état public d'un joueur possédant le nombre de billets et de cartes donnés, et s'étant emparé des routes données.
     * 
     * @param ticketCount
     *          nombre de billets du joueur.
     * @param cardCount
     *          nombre de cartes du joueurs.
     * @param routes
     *          routes en possession du joueur.
     * @exception IllegalArgumentException
     *          si le nombre de billets ou le nombre de cartes est strictement négatif (< 0).
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) {
        
        Preconditions.checkArgument(ticketCount >= 0);
        Preconditions.checkArgument(cardCount >= 0);
        
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        
        if (routes != null)
        	this.routes = List.copyOf(routes);  // copyOf nécessite une valeur non nulle
        else
        	this.routes = null;
        
        int sommeLgrRoutes = 0;
        int sommeCtrRoutes = 0;

        // Devrait lever une exception pour null. Mais d'après Piazza, pas besoin de tester pour route = null.
        //
        for (Route route : this.routes) {
            sommeLgrRoutes += route.length();
            sommeCtrRoutes += route.claimPoints();
        }
        
        this.carCount = Constants.INITIAL_CAR_COUNT - sommeLgrRoutes;        
        this.claimPoints = sommeCtrRoutes;
    }
    
    /**
     * Retourne le nombre de billets que possède le joueur.
     * 
     * @return le nombre de billets que possède le joueur.
     */
    public int ticketCount() {
        return ticketCount;
    }
    
    /**
     * Retourne le nombre de cartes que possède le joueur.
     * 
     * @return le nombre de cartes que possède le joueur.
     */
    public int cardCount() {
        return cardCount;
    }
    
    /**
     * Retourne les routes dont le joueur s'est emparé.
     * 
     * @return les routes dont le joueur s'est emparé.
     */
    public List<Route> routes(){
        return List.copyOf(routes);
    }
    
    /**
     * Retourne le nombre de wagons que possède le joueur.
     * 
     * @return le nombre de wagons que possède le joueur.
     */
    public int carCount() {
        return carCount;
    }
    
    /**
     * Retourne le nombre de points de construction obtenus par le joueur.
     * 
     * @return le nombre de points de construction obtenus par le joueur.
     */
    public int claimPoints() {
        return claimPoints;
    }
}