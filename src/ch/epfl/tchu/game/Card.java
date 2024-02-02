package ch.epfl.tchu.game;

import java.util.List;

import ch.epfl.tchu.Preconditions;

/**
 * Représente les différents types de cartes du jeu : les huit types de cartes wagon (un par couleur), et le type de carte locomotive.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public enum Card {

	BLACK(Color.BLACK),
	VIOLET(Color.VIOLET),
	BLUE(Color.BLUE),
	GREEN(Color.GREEN),
	YELLOW(Color.YELLOW),
	ORANGE(Color.ORANGE),
	RED(Color.RED),
	WHITE(Color.WHITE),
	LOCOMOTIVE(null);
	
    /**
     * Contient la totalité des valeurs du type énuméré, dans leur ordre de définition.
     */
	public static final List<Card> ALL = List.of(Card.values());
	
	/**
	 * Contient le nombre de valeurs du type énuméré.
	 */
	public static final int COUNT = ALL.size();
	
	/**
	 * Contient uniquement les cartes wagon, dans l'ordre de définition, c-à-d de BLACK à WHITE.
	 */
	public static final List<Card> CARS = ALL.subList(0, COUNT - 1);
	
	private final Color color;
	
	/**
	 * Construit une carte en lui associant sa couleur.
	 * 
	 * @param color
	 *         couleur de la carte.
	 */
	private Card(Color color) {
	    this.color = color;
	}
	
	/**
	 * Retourne le type de carte wagon correspondant à la couleur donnée. 
	 * 
	 * @param color
	 *         couleur donnée.
	 * @return le type de wagon correspondant.
	 */
	public static Card of(Color color) {
		
	    // Fonctionne uniquement car les valeurs des énumérations Color et Card sont définies dans le même ordre.
	    int colorIndex = Color.ALL.indexOf(color);
	    
	    // Si la couleur donnée en argument n'est pas trouvée, indexOf retourne -1.
	    Preconditions.checkArgument(colorIndex >= 0);
	    
	    return ALL.get(colorIndex);
	}
	
	/**
	 * Retourne la couleur du type de carte s'il s'agit d'un type wagon.
	 * 
	 * @return la couleur du type de carte. Retourne null s'il s'agit d'une locomotive.
	 */
	public Color color() {
		return color;
	}
}