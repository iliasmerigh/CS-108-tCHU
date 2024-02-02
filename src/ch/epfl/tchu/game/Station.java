package ch.epfl.tchu.game;

import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * Représente une gare.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Station {

	private final String name;
	private final int id;
	
	/**
	 * Construit une gare et lui attribut un nom et un unique numéro d'identification.
	 * 
	 * @param id
	 *         numéro d'identification unique à la gare.
	 * @param name
	 *         nom de la gare.
	 * @exception IllegalArgumentException
	 *         si l'id passé en paramètre est négatif.
	 */
	public Station(int id, String name) {
		
		Preconditions.checkArgument(id >= 0);
		Objects.requireNonNull(name);
		
		this.name = name;
		this.id = id;		
	}
	
	/**
	 * Retourne le numéro d'identification de la gare.
	 * 
	 * @return le numéro d'indentification de la gare.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Retourne le nom de la gare.
	 * 
	 * @return le nom de la gare.
	 */
	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name();
	}
}
