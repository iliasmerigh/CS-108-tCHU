package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * Représente un trajet.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Trip {
	
	private final Station from;
	private final Station to;
	private final int points;

	/**
	 * Construit un nouveau trajet entre les deux gares données et valant le nombre de points donné.
	 * 
	 * @param from
	 *         gare de départ.
	 * @param to
	 *         gare d'arrivée.
	 * @param points
	 *         nombre de points du trajet.
	 * @exception IllegalArgumentException
	 *         si le nombre de points donné est négatif ou nul.
	 * @exception NullPointerException
	 *         si une des deux gares est nulle.
	 */
	public Trip(Station from, Station to, int points) {
		
		Preconditions.checkArgument(points > 0);
		
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
		this.points = points;
	}
	
	/**
	 * Retourne la liste de tous les trajets possibles allant d'une des gares de la première liste (from) à l'une des gares de la seconde liste (to).
	 * A chaque trajet est associé un nombre de points.
	 * 
	 * @param from
	 *         liste des gares de départ.
	 * @param to
	 *         liste des gares d'arrivée.
	 * @param points
	 *         nombre de points du trajet en question.
	 * @return toutes les combinaisons de trajets possibles.
	 */
	public static List<Trip> all(List<Station> from, List<Station> to, int points) {
		
	    Preconditions.checkArgument(   from != null
	                                && to != null
	                                && !from.isEmpty()
	                                && !to.isEmpty());
	    
		ArrayList<Trip> tripArrayList = new ArrayList<Trip>();
		
		for (Station f : from) {
			
			for (Station t : to) {
				
				Trip trip = new Trip(f, t, points);
				
				tripArrayList.add(trip);
			}
		}
		
		return tripArrayList;
	}
	
	/**
	 * Retourne la gare de départ du trajet.
	 * 
	 * @return la gare de départ.
	 */
	public Station from() {
		return from;
	}
	
	/**
	 * Retourne la gare d'arrivée du trajet.
	 * 
	 * @return la gare d'arrivée.
	 */
	public Station to() {
		return to;
	}
	
	/**
	 * Retourne le nombre de points du trajet.
	 * 
	 * @return le nombre de points.
	 */
	public int points() {
		return points;
	}
	
	/**
	 * 
	 * Retourne le nombre de points du trajet pour la connectivité donnée.
	 * 
	 * @param connectivity
	 *         connectivité permettant de juger si le trajet est connecté ou non.
	 * @return le nombre de points du trajet si la connectivité est connectée, sinon retourne la négation du nombre de points.
	 */
	public int points(StationConnectivity connectivity) {
		return connectivity.connected(from, to) ? points : -points;
	}	
}
