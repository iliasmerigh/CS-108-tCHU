package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * Représente l'état des cartes wagon/locomotive qui ne sont pas en main des joueurs.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public final class CardState extends PublicCardState {
    
    private final Deck<Card> deck;
    private final SortedBag<Card> discardedCards;
    
    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discardedCards) {
		super(faceUpCards, deck.size(), discardedCards.size());
		this.deck = deck;
		this.discardedCards = discardedCards;
    }
    
    /**
     * Retourne un état dans lequel les 5 cartes disposées faces visibles sont les 5 premières du tas donné,
     * la pioche est constituée des cartes du tas restantes, et la défausse est vide.
     * 
     * @param deck
     *          tas donné.
     * @return  un état dans lequel les 5 cartes disposées faces visibles sont les 5 premières du tas donné,
     *          la pioche est constituée des cartes du tas restantes, et la défausse est vide.
     * @exception IllegalArgumentException
     *          si le tas donné contient moins de 5 cartes.
     */
    public static CardState of(Deck<Card> deck) {

    	Preconditions.checkArgument(deck != null);    	

    	Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);    	
    	
        return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(), deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT), SortedBag.of());
    }

    /**
     * Retourne un ensemble de cartes identique au récepteur (this),
     * si ce n'est que la carte face visible d'index slot a été remplacée par celle se trouvant au sommet de la pioche, qui en est du même coup retirée.
     * 
     * @param slot
     *          index de la carte à remplacer.
     * @return un ensemble de cartes identique au récepteur (this), où la carte face visible d'index slot a été remplacée par celle se trouvant au sommet de la pioche.
     * @exception IndexOutOfBoundsException
     *          si l'index donné n'est pas compris entre 0 (inclus) et 5 (exclus).
     * @exception IllegalArgumentException
     *          si la pioche est vide.
     */
    public CardState withDrawnFaceUpCard(int slot) {
    	
    	if ( (slot >= Constants.FACE_UP_CARDS_COUNT) || (slot < 0)) {
    		throw new IndexOutOfBoundsException();
    	}
    	
    	Preconditions.checkArgument(slot < Constants.FACE_UP_CARDS_COUNT);
    	Preconditions.checkArgument(!this.isDeckEmpty());
    	
    	ArrayList<Card> newFaceUpCards = new ArrayList<Card>(this.faceUpCards());
    	
    	newFaceUpCards.set(slot, this.topDeckCard());

    	return  new CardState(newFaceUpCards, deck.withoutTopCard(), SortedBag.of(discardedCards));

    }
   
    /**
     * Retourne la carte se trouvant au sommet de la pioche.
     * 
     * @return la carte se trouvant au sommet de la pioche.
     * @exception IllegalArgumentException
     *          si la pioche est vide.
     */
    public Card topDeckCard() {
        return (Card) deck.topCard();
    }
   
    /**
     * Retourne un ensemble de cartes identique au récepteur (this), mais sans la carte se trouvant au sommet de la pioche.
     * 
     * @return un ensemble de cartes identique au récepteur (this), mais sans la carte se trouvant au sommet de la pioche.
     * @exception IllegalArgumentException
     *          si la pioche est vide.
     */
    public CardState withoutTopDeckCard() {
    	return  new CardState(this.faceUpCards(), deck.withoutTopCard(), SortedBag.of(discardedCards));    	
    }
    
    /**
     * Retourne un ensemble de cartes identique au récepteur (this),
     * si ce n'est que les cartes de la défausse ont été mélangées au moyen du générateur aléatoire donné afin de constituer la nouvelle pioche.
     * 
     * @param rng
     *          générateur de nombres aléatoires.
     * @return un ensemble de cartes identique au récepteur (this), où les cartes de la défausse ont été mélangées.
     * @exception IllegalArgumentException
     *          si la pioche du récepteur n'est pas vide.
     */
    public CardState withDeckRecreatedFromDiscards(Random rng) {
    	
    	Preconditions.checkArgument(isDeckEmpty());
    	
    	return  new CardState(this.faceUpCards(), Deck.of(discardedCards, rng), SortedBag.of());
    }
    
    /**
     * Retourne un ensemble de cartes identique au récepteur (this), mais avec les cartes données ajoutées à la défausse.
     * 
     * @param additionalDiscards
     *          cartes à ajouter à la défausse.
     * @return un ensemble de cartes identique au récepteur (this), mais avec les cartes données ajoutées à la défausse.
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards) {
    	return  new CardState(this.faceUpCards(), this.deck, this.discardedCards.union(additionalDiscards));
    }
}
