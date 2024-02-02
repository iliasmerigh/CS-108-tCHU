package ch.epfl.tchu.game;

/**
 * Représente la « connectivité » du réseau d'un joueur,
 * c-à-d le fait que deux gares soient reliées ou non par le réseau ferroviaire du joueur en question.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public interface StationConnectivity {

    /**
     * Vérifie si les deux gares données sont reliées au réseau du joueur.
     * 
     * @param s1
     *      première gare.
     * @param s2
     *      seconde gare.
     * @return vrai ssi les gares données sont reliées par le réseau du joueur.
     */
	public abstract boolean connected(Station s1, Station s2);
}
