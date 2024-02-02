package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente un tas de cartes.
 * 
 * @author Ilias Marwane Merigh (330316)
 *
 * @param <C> type de carte du tas.
 */
public final class Deck<C extends Comparable<C>> {

    private final ArrayList<C> listCards;
    
    /**
     * Crée une instance de Deck qui pourra être retourné par la méthode of.
     */
    private Deck(ArrayList<C> cards) {
        listCards = cards;
    }
    
    /**
     * Retourne un tas de cartes ayant les mêmes cartes que le multiensemble cards, mélangées au moyen du générateur de nombres aléatoires rng.
     * 
     * @param <C>
     *          type de cartes.
     * @param cards
     *          ensemble de cartes à mélanger.
     * @param rng
     *          générateur de nombres aléatoires.
     * @return le tas de cartes mélangé aléatoirement.
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        
    	ArrayList<C> list;
    	
    	if (cards == null || cards.isEmpty()) {
    		list = new ArrayList<C> ();	
    	}
    	else
    	{
	        list = (ArrayList<C>) cards.toList();
	        Collections.shuffle(list, rng);
    	}
    	
        Deck<C> newDeck = new Deck<C>(list);
                
        return newDeck;
    }
    
    /**
     * Retourne la taille du tas, c-à-d le nombre de cartes qu'il contient.
     * 
     * @return la taille du tas.
     */
    public int size() {
        return listCards.size();
    }
    
    /**
     * Retourne vrai ssi le tas est vide.
     * 
     * @return vrai ssi le tas est vide.
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    
    /**
     * Retourne la carte au sommet du tas.
     * 
     * @return la carte au sommet du tas.
     * @exception IllegalArgumentException
     *          si le tas est vide.
     */
    public C topCard() {
        
        Preconditions.checkArgument(!isEmpty());
        
        return listCards.get(0);           
    }
    
    /**
     * Retourne un tas identique au récepteur (this) mais sans la carte au sommet.
     * 
     * @return un tas identique au récepteur (this) mais sans la carte au sommet
     * @exception
     *          si le tas est vide (retourné par withoutTopCards).
     */
    public Deck<C> withoutTopCard() {
        return withoutTopCards(1);
    }
    
    /**
     * Retourne un multiensemble contenant les count cartes se trouvant au sommet du tas.
     * 
     * @param count
     *          nombre de cartes à retourner.
     * @return un multiensemble contenant les count cartes se trouvant au sommet du tas
     * @exception IllegalArgumentException
     *          si count n'est pas compris entre 0 (inclus) et la taille du tas (incluse).
     */
    public SortedBag<C> topCards(int count) {
        
        Preconditions.checkArgument(0 <= count);
        Preconditions.checkArgument(count <= size());
        
        ArrayList<C> subList = new ArrayList<C> (listCards.subList(0, count));
        
        return SortedBag.of(subList);
    }
    
    /**
     * Retourne un tas identique au récepteur (this) mais sans les count cartes du sommet.
     * 
     * @param count
     *          nombre de cartes du sommet à ne pas retourner.
     * @return un tas identique au récepteur (this) mais sans les count cartes du sommet.
     * @exception IllegalArgumentException
     *          si count n'est pas compris entre 0 (inclus) et la taille du tas (incluse).
     */
    public Deck<C> withoutTopCards(int count) {
        
        Preconditions.checkArgument(0 <= count);
        Preconditions.checkArgument(count <= size());
        
        ArrayList<C> subList = new ArrayList<C> (listCards.subList(count, size()));
        
        Deck<C> newDeck = new Deck<C>(subList);
        
        return newDeck;
    }
}
