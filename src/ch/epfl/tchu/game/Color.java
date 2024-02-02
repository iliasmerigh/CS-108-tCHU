package ch.epfl.tchu.game;

import java.util.List;

/**
 * Représente les huit couleurs utilisées dans le jeu pour colorer les cartes wagon et les routes.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public enum Color {

	BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;
	
    /**
     * Contient la totalité des valeurs du type énuméré, dans leur ordre de définition.
     */
	public static final List<Color> ALL = List.of(Color.values());
	
	/**
     * Contient le nombre de valeurs du type énuméré.
     */
	public static final int COUNT = ALL.size();
}
