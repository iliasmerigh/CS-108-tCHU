package ch.epfl.tchu.game;

import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * Représente (une partie de) l'état des cartes wagon/locomotive qui ne sont pas en main des joueurs.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public class PublicCardState {

    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;
    
    /**
     * Construit un état public des cartes dans lequel les cartes face visible sont celles données,
     * la pioche contient deckSize cartes et la défausse en contient discardsSize.
     * 
     * @param faceUpCards
     *          cartes visibles du plateau.
     * @param deckSize
     *          nombre de carte de la pioche.
     * @param discardsSize
     *          nombre de carte de la défausse.
     * @exception IllegalArgumentException
     *          si faceUpCards ne contient pas le bon nombre d'éléments (5),
     *          ou si la taille de la pioche ou de la défausse sont négatives (< 0).
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {

    	Preconditions.checkArgument(faceUpCards != null);
    	Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT);
    	Preconditions.checkArgument(deckSize >= 0);
       	Preconditions.checkArgument(discardsSize >= 0);
           	
    	this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }
    
    /**
     * Retourne le nombre total de cartes qui ne sont pas en main des joueurs,
     * à savoir les 5 dont la face est visible, celles de la pioche et celles de la défausse.
     * 
     * @return le nombre total de cartes qui ne sont pas en main des joueurs.
     */
    public int totalSize() {
        return faceUpCards.size() + deckSize + discardsSize;
    }
    
    /**
     * Retourne les 5 cartes face visible, sous la forme d'une liste comportant exactement 5 éléments.
     * 
     * @return les 5 cartes face visible, sous la forme d'une liste comportant exactement 5 éléments.
     */
    public List<Card> faceUpCards() {
        return List.copyOf(faceUpCards);
    }
    
    /**
     * Retourne la carte face visible à l'index donné.
     * 
     * @return la carte face visible à l'index donné.
     * @exception IndexOutOfBoundsException
     *          si cet index n'est pas compris entre 0 (inclus) et 5 (exclus).
     */
    public Card faceUpCard(int slot) {
        return faceUpCards.get(Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT));
    }
    
    /**
     * Retourne la taille de la pioche.
     * 
     * @return la taille de la pioche.
     */
    public int deckSize() {
        return deckSize;
    }
    
    /**
     * Retourne vrai ssi la pioche est vide.
     * 
     * @return vrai ssi la pioche est vide.
     */
    public boolean isDeckEmpty() {
        return deckSize == 0;
    }
    
    /**
     * Retourne la taille de la défausse.
     * 
     * @return la taille de la défausse.
     */
    public int discardsSize() {
        return discardsSize;
    }
}
