package ch.epfl.tchu;

/**
 * Offre la possibilité de s'assurer qu'une proposition passée en argument est vraie.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class Preconditions {

    /**
     * Empêche l'instanciation de la classe.
     */
	private Preconditions() {}
	
	/**
	 * Vérifie si la proposition passée est vraie. Si non, lève une exception.
	 * 
	 * @param shouldBeTrue : proposition à vérifier.
	 * @exception IllegalArgumentException : si la proposition donnée en paramètre est fausse.
	 */
	public static void checkArgument (boolean shouldBeTrue) {
		
		if (!shouldBeTrue) {
			throw new IllegalArgumentException();
		}
	}
}
