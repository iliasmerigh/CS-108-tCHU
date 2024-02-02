package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Représente une partition (aplatie) de gares.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class StationPartition implements StationConnectivity {

    private final int[] representativeFlat;
    
    private StationPartition(int[] representatives) {
        representativeFlat = representatives.clone();
    }
    
    /**
     * Mise en oeuvre de la methode connected de l'interface StationConnectivity.
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        
        if ( (s1.id() < representativeFlat.length) && (s2.id() < representativeFlat.length))        
            return representativeFlat[s1.id()] == representativeFlat[s2.id()];
        else
            return s1.id() == s2.id();          
    }
    
    /**
     * Représente un bâtisseur de partition de gare.
     * 
     * @author Ilias Marwane Merigh (330316)
     */
    public static final class Builder {
        
        private int [] representative;
        
        /**
         * Construit un bâtisseur de partition d'un ensemble de gares dont l'identité est comprise entre 0 (inclus) et stationCount (exclus).
         * 
         * @param stationCount
         *          numéro d'identité maximale des gares.
         * @exception IllegalArgumentException
         *          si stationCount est strictement négatif (< 0).
         */
        public Builder(int stationCount) {
            
            Preconditions.checkArgument(stationCount >= 0);
            
            representative = new int[stationCount];
            
            // Initialement chaque gare est représentant du sous-ensemble qui la 
            // contient (et ne contient qu’elle)
            
            for (int stationID = 0; stationID < stationCount; stationID++)
                representative[stationID] = stationID;
        }
        
        /**
         * Joint les sous-ensembles contenant les deux gares passées en argument, en « élisant »
         * l'un des deux représentants comme représentant du sous-ensemble joint, et retourne le bâtisseur (this).
         * 
         * @param s1
         *          première gare.
         * @param s2
         *          seconde gare.
         * @return le bâtisseur (this)
         */
        public Builder connect(Station s1, Station s2) {

            if ( (s1.id() < representative.length) && (s2.id() < representative.length))  
            {
                int rep1 = getRepresentative(s1.id());
                int rep2 = getRepresentative(s2.id());
                
                representative[rep1] = rep2;
            }
            
            return this;
        }
        
        private int getRepresentative(int stationID) {

            int rep = stationID;
            
            // Au lieu d'une boucle while, on fait une boucle for pour nous assurer que
            // les erreurs ne provoquent pas de boucles infinies.
            //
            for (int iTry =0; iTry <= representative.length; iTry++) {
                
                if (rep != representative[rep])
                    rep = representative[rep];
                else
                    return rep;
            }

            
            // Ne devrait jamais arriver.
            //
            return -1;
        }
        
        /**
         * Retourne la partition aplatie des gares correspondant à la partition profonde en cours de construction par ce bâtisseur.
         * 
         * @return la partition aplatie des gares correspondant à la partition profonde en cours de construction par ce bâtisseur.
         */
        public StationPartition build() {

            for (int stationID =0; stationID < representative.length; stationID++)
                representative[stationID] = getRepresentative(stationID);
            
            return new StationPartition(representative);
        }
    }
}