package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.tchu.Preconditions;

/**
 * Représente un chemin dans le réseau d'un joueur.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Trail {
    
    private final Station from;
    private final Station to;
    private final List<Route> routes;
    private final int length;
    
    /**
     * Construit un chemin allant de la gare from à la gare to en passant par les routes donées.
     * 
     * @param from
     *          gare de départ.
     * @param to
     *          gare d'arrivée.
     * @param routes
     *          routes composants le chemin.
     */
    private Trail(Station from, Station to, List<Route> routes) {
        
        this.from = from;
        this.to = to;
        this.routes = routes;
        this.length = calculateLength();
    }
    
    /**
     * Calcul la longueur du chemin à partir de ses routes.
     * 
     * @return la longueur du chemin.
     * @exception IllegalArgumentException
     *          si le chemin a une discontinuité, ou si la dernière route ne mène pas à la gare finale.
     */
    private int calculateLength() {
        
        Station nextStation = from;
        int lengthSum = 0;
        
        if (from == null)
            return 0;
        
        for (Route r: routes) {
            
            if (!r.stations().contains(nextStation))
                throw new IllegalArgumentException();

            nextStation = r.stationOpposite(nextStation);
            
            lengthSum += r.length();
        }
        
        if (!nextStation.equals(to))
            throw new IllegalArgumentException();
        
        return lengthSum;
    }
    
    /**
     * Retourne le plus long chemin du réseau constitué des routes données.
     * 
     * @param routes
     *          routes du chemin.
     * @return  Le plus long chemin du réseau.
     *          S'il y a plusieurs chemins de longueur maximale, celui qui est retourné n'est pas spécifié.
     *          Si la liste des routes données est vide, retourne un chemin de longueur zéro, dont les gares sont toutes deux égales à null.
     */
    public static Trail longest(List<Route> routes) {

        if (routes == null || routes.size() == 0) {
            return new Trail(null, null, null);
        }
        
        ArrayList<Trail> cs = new ArrayList<Trail>();       
        Trail longestTrail = null;

        for (Route r: routes) {
            Trail r1 = new Trail(r.station1(), r.station2(), List.of(r));
            Trail r2 = new Trail(r.station2(), r.station1(), List.of(r));
            
            cs.add(r1);
            cs.add(r2);
            
            if ((longestTrail == null) || (longestTrail.length < r1.length))
                longestTrail = r1;
        }

        
        while(!cs.isEmpty()) {
            ArrayList<Trail> csprime = new ArrayList<Trail>();

            for (Trail c: cs) {
                ArrayList<Route> rs = new ArrayList<Route>();           

                for (Route r: routes) {
                    if (!c.routes.contains(r) && (c.to.equals(r.station1()) || c.to.equals(r.station2()))) 
                        rs.add(r);
                }           

                for (Route r: rs) {
                    ArrayList<Route> extendedRoute = new ArrayList<Route>(c.routes);
                    extendedRoute.add(r);
                    Trail newTrail = new Trail(c.station1(), r.stationOpposite(c.station2()), extendedRoute);
                    csprime.add(newTrail);
                    
                    if (longestTrail.length() < newTrail.length()) {
                        longestTrail  = newTrail;
                    }
                }
            }
            
            cs = csprime;
            
        }
 
        return longestTrail; 
    }
    
    /**
     * Retourne la longueur du chemin.
     * 
     * @return la longueur du chemin.
     */
    public int length() {
        return length;
    }
    
    /**
     * Retourne la première gare du chemin.
     * 
     * @return la première gare du chemin. Retourne null ssi le chemin est de longueur zéro.
     */
    public Station station1() {        
        return from;        
    }
    
    /**
     * Retourne la dernière gare du chemin.
     * 
     * @return la dernière gare du chemin. Retourne null ssi le chemin est de longueur zéro.
     */
    public Station station2() {        
        return to;
    }
    
    @Override
    public String toString() {
        return computeText();
    }
    
    /**
     * Retourne la représentation textuelle du chemin, composé de toutes les gares du chemin, suivi de la longueur du chemin entre parenthèses.
     * Exemple : Lucerne - Berne - Neuchâtel - Soleure - Berne - Fribourg (13)
     *  
     * @return la représentation textuelle du chemin.
     */
    private String computeText() {
        
        if (length() == 0) {
            return "";
        }
        
        ArrayList<String> stationNames = new ArrayList<String>();

        stationNames.add(from.name());
        Station nextStation = from;
        
        for (Route r: routes) {
                                    
            nextStation = r.stationOpposite(nextStation);

            stationNames.add(nextStation.name());
        }        
                
        return String.join(" - ", stationNames) + " (" + length() + ")";
    }
}
