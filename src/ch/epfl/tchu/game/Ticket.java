package ch.epfl.tchu.game;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import ch.epfl.tchu.Preconditions;

/**
 * Représente un billet.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
	private final String computedText;
	
	/**
	 * Construit un billet constitué de la liste de trajets donnée.
	 * 
	 * @param trips
	 *         liste de trajets donnés.
	 * @exception IllegalArgumentException
	 *         si la liste de trajets est nulle ou vide, ou alors si toutes les gares de départ des trajets n'ont pas le même nom.
	 */
	public Ticket(List<Trip> trips) {
		
	    Preconditions.checkArgument(trips != null & trips.size() > 0);
		
		final String fromName = trips.get(0).from().name();
		
		for (Trip trip : trips) {
			
			Preconditions.checkArgument(trip.from().name() == fromName);
		}
		
		this.trips = trips;
		
		computedText = computeText(trips);
	}
	
	/**
	 * Construit un billet constitué d'un unique trajet entre deux gares.
	 * 
	 * @param from
	 *         gare de départ.
	 * @param to
	 *         gare d'arrivée.
	 * @param points
	 *         nombre de points du trajet.
	 */
	public Ticket(Station from, Station to, int points) {
				
		this(List.of(new Trip(from, to, points)));
	}
	
	/**
	 * Retourne la représentation textuelle du billet.
	 * 
	 * @return la représentation textuelle du billet.
	 */
	public String text() {
		return computedText;
	}
	
	/**
	 * Calcul la représentation textuelle du billet comme ceci :
	 * 
	 * De ville à ville : nomVilleDepart - nomVilleArrivée (nombreDePointsDuTrajet)
	 * Exemple : Lausanne - Saint-Gall (13)
	 * 
	 * De ville à pays : nomVilleDepart - {nomPaysDepart1 (nombreDePointsDuTrajet1), nomPaysDepart2 (nombreDePointsDuTrajet2)...}
	 * Exemple : Berne - {Allemagne (6), Autriche (11), France (5), Italie (8)}
	 * 
	 * De pays à pays : similaire à "de ville à pays"
	 * Exemple : France - {Allemagne (5), Autriche (14), Italie (11)}
	 */
	private static String computeText(List<Trip> trips) {
	    
	    if (trips.size() == 1) {
	        
	        return (trips.get(0).from().name() + " - " + trips.get(0).to().name() + " (" + trips.get(0).points() + ")");
	        
	    } else {
	        
	        TreeSet<String> destinations = new TreeSet<String>();
	        
	        for (int i = 0; i < trips.size(); i++) {
	            
	            destinations.add(String.format("%s (%d)", trips.get(i).to().name(), trips.get(i).points()));	            
	        }
	        
	        return trips.get(0).from().name() + " - {" + String.join(", ", destinations) + "}";	        
	    }
	}
	
	/**
	 * Retourne nombre de points que vaut le billet.
	 * 
	 * @param connectivity
	 *         connectivité du joueur possédant le billet.
	 * @return le nombre de points que vaut le billet.
	 */
	public int points(StationConnectivity connectivity) {
			    
	    if (trips.size() == 1) {
	        return trips.get(0).points(connectivity);
	        
	    } else {
	        	  
	        int pnts = Integer.MIN_VALUE;
	        
	        for (Trip trip : trips) {
	            pnts = Math.max(pnts, trip.points(connectivity));
	        }
	        
	        return pnts;
	    }
	}
	
	@Override
	public int compareTo(Ticket that) {
		return text().compareTo(that.text());
	}

	@Override
	public String toString() {
		return text();
	}
}
