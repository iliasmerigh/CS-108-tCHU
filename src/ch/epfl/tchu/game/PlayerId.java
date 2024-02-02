package ch.epfl.tchu.game;

import java.util.List;

/**
 * Représente l'identité d'un joueur.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public enum PlayerId {
    
    PLAYER_1, PLAYER_2;
       
    /**
     * Contient la totalité des valeurs du type énuméré, dans leur ordre de définition.
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    
    /**
     * Contient le nombre de valeurs du type énuméré.
     */
    public static final int COUNT = ALL.size();
    
    /**
     * Retourne l'identité du joueur qui suit celui auquel on l'applique, c-à-d PLAYER_2 pour PLAYER_1, et PLAYER_1 pour PLAYER_2.
     * 
     * @return l'identité du joueur qui suit celui auquel on l'applique, c-à-d PLAYER_2 pour PLAYER_1, et PLAYER_1 pour PLAYER_2.
     */
    public PlayerId next() {
        
        return (this == PlayerId.PLAYER_1) ? PlayerId.PLAYER_2 : PlayerId.PLAYER_1;
    }
}