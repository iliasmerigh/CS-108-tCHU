package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente une route.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Route {
    
    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;
    
    /**
     * Représentant les deux niveaux auquel une route peut se trouver.
     */
    public enum Level {
        
        OVERGROUND, UNDERGROUND
    }

    /**
     * Construit une route avec les paramètres donnés.
     * 
     * @param id
     *          identité unique de la route.
     * @param station1
     *          première gare de la route.
     * @param station2
     *          seconde gare de la route.
     * @param length
     *          taille de la route.
     * @param level
     *          niveau de la route : soit souterrain, soit à la surface.
     * @param color
     *          couleur de la route.
     * @exception IllegalArgumentException
     *          si les deux gares sont égales, ou si la longueur n'est pas comprise dans les limites acceptables (cf. l'interface Constants).
     * @exception NullPointerException
     *          si l'identité, l'une des deux gares ou le niveau sont nuls.
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(Constants.MIN_ROUTE_LENGTH <= length);             
        Preconditions.checkArgument(length <= Constants.MAX_ROUTE_LENGTH);
        
        Objects.requireNonNull(id);
        Objects.requireNonNull(station1);
        Objects.requireNonNull(station2);
        Objects.requireNonNull(level);
        
        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;
    }
    
    /**
     * Retourne l'identité de la route.
     * 
     * @return l'identité de la route.
     */
    public String id() {
        return id;
    }
    
    /**
     * Retourne la première gare de la route.
     * 
     * @return la première gare de la route.
     */
    public Station station1() {
        return station1;
    }
    
    /**
     * Retourne la seconde gare de la route.
     * 
     * @return la seconde gare de la route.
     */
    public Station station2() {
        return station2;
    }
    
    /**
     * Retourne la longueur de la route.
     * 
     * @return la longueur de la route.
     */
    public int length() {
        return length;
    }
    
    /**
     * Retourne le niveau auquel se trouve la route.
     * 
     * @return  le niveau auquel se trouve la route.
     */
    public Level level() {
        return level;
    }
    
    /**
     * Retourne la couleur de la route.
     * 
     * @return la couleur de la route. Retourne null s'il s'agit d'une couleur neutre.
     */
    public Color color() {
        return color;
    }
    
    /**
     * Retourne la liste des deux gares de la route dans l'ordre station1, station2.
     * 
     * @return la liste des deux gares de la route.
     */
    public List<Station> stations(){
        return Arrays.asList(station1, station2);                        
    }
    
    /**
     * Retourne la gare de la route qui n'est pas celle donnée (gare complémentaire).
     * 
     * @param station
     *          gare donnée.
     * @return la gare de la route qui n'est pas celle donnée.
     * @exception IllegalArgumentException
     *          si la gare donnée n'est ni la première ni la seconde gare de la route
     */
    public Station stationOpposite(Station station) {
        
        Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
        
        return (station == station1) ? station2 : station1;        
    }
    
    /**
     * Retourne la liste de tous les ensembles de cartes qui pourraient être joués pour tenter de s'emparer de la route,
     * triée par ordre croissant de nombre de cartes locomotive, puis par couleur.
     * 
     * @return la liste de tous les ensembles de cartes qui pourraient être joués pour tenter de s'emparer de la route, triée.
     */
      public List<SortedBag<Card>> possibleClaimCards() {
            
            ArrayList<SortedBag<Card>> theList = new ArrayList<SortedBag<Card>>();
            
            int locomotiveCardCountMax = (level == Level.UNDERGROUND)? length : 0;          
            
            for (int locomotiveCardCount = 0;  locomotiveCardCount <= locomotiveCardCountMax; locomotiveCardCount++) {
            
                if (color != null) {
                    var cardsBuilder = new SortedBag.Builder<Card>();
                    cardsBuilder.add(length - locomotiveCardCount, Card.of(color));
                    cardsBuilder.add(locomotiveCardCount, Card.LOCOMOTIVE);
                    theList.add(cardsBuilder.build());
                } else {
                    
                    if (locomotiveCardCount == length) {
                        var cardsBuilder = new SortedBag.Builder<Card>();
                        cardsBuilder.add(locomotiveCardCount, Card.LOCOMOTIVE);
                        theList.add(cardsBuilder.build());                          
                        
                    } else {
                        for (Color clr: Color.ALL) {
                            var cardsBuilder = new SortedBag.Builder<Card>();
                            cardsBuilder.add(length - locomotiveCardCount, Card.of(clr));
                            cardsBuilder.add(locomotiveCardCount, Card.LOCOMOTIVE);
                            theList.add(cardsBuilder.build());                          
                        }
                    }               
                } 
            }
                        
            return theList;         
       }

    /**
     * Retourne le nombre de cartes additionnelles à jouer pour s'emparer de la route (en tunnel),
     * sachant que le joueur a initialement posé les cartes claimCards et que les trois cartes tirées du sommet de la pioche sont drawnCards.
     * 
     * @param claimCards
     *          cartes initialements posées par le joueur.
     * @param drawnCards
     *          trois cartes tirées du sommet de la pioche.
     * @return le nombre de cartes additionnelles à jouer.
     * @exception IllegalArgumentException
     *          si cette route n'est pas un tunnel, ou si drawnCards ne contient pas exactement 3 cartes.
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){
 
        Preconditions.checkArgument(level == Level.UNDERGROUND);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);
                
        int matchColor = 0;
        int matchLocomotive = 0;
        
        for (Card dc: drawnCards)           
            if (dc.color() != null) {                                // Si drawn card est un wagon.
                for (Card cc: claimCards) {                          // Boucle car on ne sait si les claim cards posées par le joueur sont triée,
                                                                     // elles peuvent commencer par une locomotive.
                    if (dc.color() == cc.color()) {
                        matchColor += 1;
                        break;                                       // On sort de la boucle car les wagons restants des claim cards sont de la même couleur.
                    }
                }
            } else {                                                 // Si drawn card est une locomotive.
                matchLocomotive += 1;
            }        
        
        return matchColor + matchLocomotive ;
    }
    
    /**
     * Retourne le nombre de points de construction qu'un joueur obtient lorsqu'il s'empare de la route.
     * 
     * @return le nombre de points de construction qu'un joueur obtient lorsqu'il s'empare de la route.
     */
    public int claimPoints() {
                
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
}
