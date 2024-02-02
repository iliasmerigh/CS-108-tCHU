package ch.epfl.tchu.net;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente ce que l'on nomme un serde, à savoir un objet capable de sérialiser et désérialiser des valeurs d'un type donné.
 * 
 * @author Ilias Marwane Merigh (330316)
 */

// public interface important (cf. rendu intermediaire)
// verifier les java docs
// il y a un commentaire en vert en bas

public interface Serde<C> {

	public abstract String serialize(C payload);

	public abstract C deserialize(String payload);
	
	/**
	 * Prend en arguments une fonction de sérialisation et une fonction de désérialisation, et retourne le serde correspondant.
	 * 
	 * @param <T>
	 *         paramètre de type de la méthode.
	 * @param ser
	 *         fonction de sérialisation de type Function<T, String>.
	 * @param deser
	 *         fonction de désérialisation de type Function<String, T>.
	 * @return le serde correspondant.
	 */
    public static <T> Serde<T> of (Function<T, String> ser, Function<String, T> deser) {

		Preconditions.checkArgument(ser != null);
		Preconditions.checkArgument(deser != null);

    	return new Serde<T>(){
    	    
			@Override
			public String serialize(T payload) {
				return ser.apply(payload);
			}

			@Override
			public T deserialize(String payload) {
				return deser.apply(payload);
			}		
		};
	}

	/**
	 * Prend en argument la liste de toutes les valeurs d'un ensemble de valeurs énuméré et retoure le serde correspondant.
	 *   
	 * @param <T>
	 *         paramètre de type de la méthode.
	 * @param collection
	 *         liste de toutes les valeurs d'un ensemble de valeurs énuméré.
	 * @return le serde correspondant.
	 */
    public static <T> Serde<T> oneOf (List<T> collection) {

		Preconditions.checkArgument(collection != null);
		Preconditions.checkArgument(!collection.isEmpty());
		
    	return Serde.of(elem -> Integer.toString(collection.indexOf(elem)), index -> collection.get(Integer.parseInt(index)));	
	}    

	/**
	 * Prend en argument un serde et un caractère de séparation et retourne un serde capable de (dé)sérialiser 
	 * des listes de valeurs (dé)sérialisées par le serde donné.
	 * 
	 * @param <T>
	 *         paramètre de type de la méthode.
	 * @param oneSerde
	 *         serde donné.
	 * @param separator
	 *         caractère de séparation.
	 * @return un serde capable de (dé)sérialiser des listes de valeurs (dé)sérialisées par le serde donné.
	 */
    public static <T> Serde<List<T>> listOf (Serde<T> oneSerde, String separator) {

		Preconditions.checkArgument(oneSerde != null);
		Preconditions.checkArgument(!separator.isEmpty());
    	
    	return new Serde<List<T>>() {
			@Override
			public String serialize(List<T> payload) {
				List<String> serElements = new ArrayList<String>();

				payload.forEach(elem -> serElements.add(oneSerde.serialize(elem)));
						
				return String.join(separator, serElements);
			}

			@Override
			public List<T> deserialize(String payload) {
						   	 	
//				List<String> serElements = Arrays.asList(payload.split("[" + separator + "]"));
				List<String> serElements = Arrays.asList(payload.split(Pattern.quote(separator)));
				List<T> deserElements = new ArrayList<T>();
				serElements.forEach(elem -> deserElements.add(oneSerde.deserialize(elem)));
				
				return deserElements;
			}		
		};	
	}    
        
	/**
	 * Prend en argument un serde et un caractère de séparation et retourne un serde capable de (dé)sérialiser 
     * des multiensembles triés (SortedBag) (dé)sérialisées par le serde donné.
	 * 
	 * @param <T>
	 *         paramètre de type de la méthode.
	 * @param oneSerde
	 *         serde donné.
	 * @param separator
	 *         caractère de séparation.
	 * @return un serde capable de (dé)sérialiser des multiensembles triés (SortedBag) (dé)sérialisées par le serde donné.
	 */
    public static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf (Serde<T> oneSerde, String separator) {

		Preconditions.checkArgument(oneSerde != null);
		Preconditions.checkArgument(!separator.isEmpty() );
    	
    	return new Serde<SortedBag<T>>() {
    	    
			@Override
			public String serialize(SortedBag<T> payload) {
				List<String> serElements = new ArrayList<String>();

				payload.forEach(elem -> serElements.add(oneSerde.serialize(elem)));
						
				return String.join(separator, serElements);
			}

			@Override
			public SortedBag<T> deserialize(String payload) {
//				List<String> serElements = Arrays.asList(payload.split("[" + separator + "]"));
				List<String> serElements = Arrays.asList(payload.split(Pattern.quote(separator)));
				List<T> deserElements = new ArrayList<T>();
				serElements.forEach(elem -> deserElements.add(oneSerde.deserialize(elem)));
				
				return SortedBag.of(deserElements);
			}		
		};	
	}   
}
