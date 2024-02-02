package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import ch.epfl.tchu.game.Player.TurnKind;


/**
 * Représente une partie de tCHu
 *
 * @author Ilias Marwane Merigh (330316)
 */
public final class Game {
       
    /**
     * Constructeur privé pour que la classe soit non-instanciable.
     *
     * @author Ilias Marwane Merigh (330316)
     */
    private Game () {}
   
    private final static int IN_GAME_CARD_DRAW = 2;
   
    /**
     * Fait jouer une partie de tCHu aux joueurs donnés, dont les noms figurent dans
     * la table playerNames ; les billets disponibles pour cette partie sont ceux de
     * tickets, et le générateur aléatoire rng est utilisé pour créer l'état initial
     * du jeu et pour mélanger les cartes de la défausse pour en faire une
     * nouvelle pioche quand cela est nécessaire.
     *
     * @param players
     *         joueurs jouant la partie.
     * @param playerNames
     *         noms des joueurs jouant la partie.
     * @param tickets
     *         billets disponibles pour la partie.
     * @param rng
     *         générateur aléatoire.
     * @exception IllegalArgumentException
     *         si l'une des deux tables associatives a une taille différente de 2.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {
       
        Map<PlayerId, Info> playerInfos = new EnumMap<PlayerId, Info>(PlayerId.class);
        Map<PlayerId, String> playerMessages = new EnumMap<PlayerId, String>(PlayerId.class);
       
        Preconditions.checkArgument((players.size() == PlayerId.COUNT)
                                     && (playerNames.size() == PlayerId.COUNT));
       
        // Après cet appel, gameState va contenir l'état initial d'une partie de tCHu:
        // Quatre cartes sont données a chaque joueur.
        // La pioche contient ALL_CARDS moins les cartes dans les mains des joeurs.
        // Le premier joueur est selectionné au hasard.
        //
        GameState gameState = GameState.initial(tickets, rng);
        Player currentPlayer = players.get(gameState.currentPlayerId());
       
        // Créer une playerInfo par joueur et faire un initPlayer sur les joueurs.
        //
        for (PlayerId playerID: PlayerId.ALL) {
            String playerName =playerNames.get(playerID);          
            playerInfos.put(playerID, new Info(playerName));
           
            Player player = players.get(playerID);
            player.initPlayers(playerID, playerNames);
        }
        Info currentPlayerInfo = playerInfos.get(gameState.currentPlayerId());

        // Informer les joueurs de qui va jouer en premier.
        //
        receiveInfoTwoPlayers(players, currentPlayerInfo.willPlayFirst());
       
        // On donne les tickets aux joueurs.  D'apres @943, ceci va faire apparaitre une fenetre
        // proposant au joueur de choisir des billets.
        //
        for (PlayerId playerID: PlayerId.ALL) {

            Player player = players.get(playerID);

            // gameState va nous donner les tickets du sommet de la pioche.
            //
            SortedBag<Ticket> playerTickets = gameState.topTickets(Constants.INITIAL_TICKETS_COUNT);
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
            player.updateState(gameState, gameState.playerState(playerID));
                                   
            // Communiquer les tickets initiaux au joueur.
            //
            player.setInitialTicketChoice(playerTickets);
        }
       
        // Une boucle separée pour l'appel a chooseInitialTickets.
        //
        for (PlayerId playerID: PlayerId.ALL) {

            Player player = players.get(playerID);
            Info playerInfo = playerInfos.get(playerID);
           
            // Le joueur indique les tickets choisis.
            //
            SortedBag<Ticket> playerTickets = player.chooseInitialTickets();
            gameState.withInitiallyChosenTickets(playerID, playerTickets);
            player.updateState(gameState, gameState.playerState(playerID));

            // On va sauvegarder les messages jusqu'a ce que les deux joeurs choisissent
            // leurs tickets initiaux.
            //
            playerMessages.put(playerID, playerInfo.keptTickets(playerTickets.size()));
        }

        // Informer les joueurs du nombre de tickets gardés par chaque joeur.
        //
        for (PlayerId playerID: PlayerId.ALL) {
            receiveInfoTwoPlayers(players, playerMessages.get(playerID));
        }
                   
        // lastTurnCompleted ne sera true (ou false) que lorsque le dernier joueur aura joué.
        //
        PlayerId lastPlayerId = null;
        PlayerId currentPlayerId;
               
        while(true) {
           
            currentPlayerId = gameState.currentPlayerId();
            currentPlayer = players.get(currentPlayerId);
            currentPlayerInfo = playerInfos.get(currentPlayerId);

            // INFO: Le tour d'un joueur commence.
            //
            receiveInfoTwoPlayers(players, currentPlayerInfo.canPlay());

            updatePlayerStates(players, gameState);
                       
            Player.TurnKind turnKind = currentPlayer.nextTurn();
           
            // Le joueur courant désire tirer des billets
            //
            switch (turnKind) {
           
           case DRAW_TICKETS:
             
               // Prendre les 3 (IN_GAME_TICKETS_COUNT) tickets du sommet de la pioche.
               //
               SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
             
               // INFO: Le joueur actuel a tiré des billets.
               //              
               receiveInfoTwoPlayers(players, currentPlayerInfo.drewTickets(drawnTickets.size()));

               // INFO:  joueur choisit des billets.
               //                          
               SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(drawnTickets);
               receiveInfoTwoPlayers(players, currentPlayerInfo.keptTickets(chosenTickets.size()));
                             
               gameState = gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);              

               break;
           
           // Le joueur courant désire tirer des cartes
           //          
           case DRAW_CARDS:
             
               for (int intCount = 0; intCount < IN_GAME_CARD_DRAW; intCount++) {
                   int slot = currentPlayer.drawSlot();

                   updatePlayerStates(players, gameState);
                 
                   if (gameState.cardState().isDeckEmpty()) {
                       gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                   }
                 
                   if (slot == Constants.DECK_SLOT) {  // Tirer la carte de la pioche
                       gameState = gameState.withBlindlyDrawnCard();
                       receiveInfoTwoPlayers(players, currentPlayerInfo.drewBlindCard());

                   } else {                            // Tirer une carte retournée
                     
                       Card faceUpCard = gameState.cardState().faceUpCard(slot);
                     
                       gameState = gameState.withDrawnFaceUpCard(slot);
                       receiveInfoTwoPlayers(players, currentPlayerInfo.drewVisibleCard(faceUpCard));  
                   }
               }

               break;
               
           // Le joueur courant désire s'emparer d'une route
           //          
           case CLAIM_ROUTE:
             
               Route claimedRoute = currentPlayer.claimedRoute();
               SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();
             
               updatePlayerStates(players, gameState);

               if (claimedRoute.level() == Route.Level.OVERGROUND) {
                   gameState = gameState.withMoreDiscardedCards(initialClaimCards);
                 
                   gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
 
                   receiveInfoTwoPlayers(players, currentPlayerInfo.claimedRoute(claimedRoute, initialClaimCards));    

               } else {
                 
                   receiveInfoTwoPlayers(players, currentPlayerInfo.attemptsTunnelClaim(claimedRoute, initialClaimCards));
                 
                   ArrayList<Card> listDrawnCards = new ArrayList<Card>();
                 
                   for (int intCard = 0; intCard < Constants.ADDITIONAL_TUNNEL_CARDS; intCard++) {
                                         
                       if (gameState.cardState().isDeckEmpty()) {
                           gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                       }
                                             
                       listDrawnCards.add(gameState.topCard());
                       gameState = gameState.withoutTopCard();                    
                   }
                 
                   SortedBag<Card> drawnCards = SortedBag.of(listDrawnCards);

                   gameState = gameState.withMoreDiscardedCards(drawnCards);
                   updatePlayerStates(players, gameState);
                 
                   int additionalCost = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
                   receiveInfoTwoPlayers(players, currentPlayerInfo.drewAdditionalCards(drawnCards, additionalCost));
                 
                   boolean claimSuccessful = false;
                 
                   if (additionalCost  <= 3) {
                         
                       SortedBag<Card> usedCards = initialClaimCards.union(drawnCards);
                       SortedBag<Card> additionalCards = SortedBag.of();
                     
                       if (additionalCost > 0) {
                           PlayerState currentPlayerState = gameState.currentPlayerState();

                           List<SortedBag<Card>> lstCards = currentPlayerState.possibleAdditionalCards(additionalCost, initialClaimCards);
                         
                           if (lstCards.size() == 0)
                               claimSuccessful = false;
                           else
                               additionalCards = currentPlayer.chooseAdditionalCards(lstCards);
                       }
                     
                       if (additionalCards != null) {
                           usedCards = initialClaimCards.union(additionalCards);                          
                           gameState = gameState.withMoreDiscardedCards(usedCards);
                           gameState = gameState.withClaimedRoute(claimedRoute, usedCards);
                         
                           updatePlayerStates(players, gameState);
 
                           receiveInfoTwoPlayers(players, currentPlayerInfo.claimedRoute(claimedRoute, usedCards));
                           claimSuccessful = true;
                       }
                   }
                 
                   if (!claimSuccessful)
                       receiveInfoTwoPlayers(players, currentPlayerInfo.didNotClaimRoute(claimedRoute));                    
                   
                   break;
                   
            }  // switch
           
            // La partie se s'achève quand:
            //    lastPlayerId à été établi au tour précédent.
            //    lastPlayerId vient de jouer.
            if ((lastPlayerId != null) &&  (gameState.currentPlayerId() == lastPlayerId))
            {
                break;
            }
           
            if (gameState.lastTurnBegins() && (lastPlayerId ==  null) ) {
                int carCount = gameState.currentPlayerState().carCount();
                receiveInfoTwoPlayers(players, currentPlayerInfo.lastTurnBegins(carCount));
            }

            // Termine le tour du joueur courant: le joueur courant devient celui qui suit le joueur courant actuel;
            // de plus, si lastTurnBegins retourne vrai, le joueur courant actuel devient le dernier joueur.
            //
            gameState = gameState.forNextTurn();
            lastPlayerId = gameState.lastPlayer();
           
        } // end while()

        // Infos sur the points et bonus.
        //
        int pointsPlayer1 = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int pointsPlayer2 = gameState.playerState(PlayerId.PLAYER_2).finalPoints();
   
        Trail longestTrail1 = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
        Trail longestTrail2 = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());
       
        if (longestTrail2.length() > longestTrail1.length())
            receiveInfoTwoPlayers(players, playerInfos.get(PlayerId.PLAYER_2).getsLongestTrailBonus(longestTrail2));

        if (longestTrail1.length() > longestTrail2.length())
            receiveInfoTwoPlayers(players, playerInfos.get(PlayerId.PLAYER_1).getsLongestTrailBonus(longestTrail1));            
       
        if (pointsPlayer1 == pointsPlayer2) {
            ArrayList<String> names = new ArrayList<String>();
            names.add(playerNames.get(PlayerId.PLAYER_1));
            names.add(playerNames.get(PlayerId.PLAYER_2));
            receiveInfoTwoPlayers(players, Info.draw(names, pointsPlayer1));
        }
        else if (pointsPlayer1 > pointsPlayer2) {
            Info playerInfo = playerInfos.get(PlayerId.PLAYER_1);          
            receiveInfoTwoPlayers(players, playerInfo.won(pointsPlayer1, pointsPlayer2));          
        } else {
            Info playerInfo = playerInfos.get(PlayerId.PLAYER_2);          
            receiveInfoTwoPlayers(players, playerInfo.won(pointsPlayer2, pointsPlayer1));                      
        }
    }
   
    /**
     * Appelle la methode receiveInfo pour les deux joueurs avec le meme
     * message.
     *  
     * @param msg
     *         message envoyé aux deux joueurs.
     */
    private static void receiveInfoTwoPlayers(Map<PlayerId, Player> players, String msg) {

        players.get(PlayerId.PLAYER_1).receiveInfo(msg);
        players.get(PlayerId.PLAYER_2).receiveInfo(msg);    
    }
   
    /**
     * Fait une update aux deux joueurs en meme temps.
     *
     * @param players : Map des joueurs  *
     * @param gameState : GameState actual
     */
    private static void updatePlayerStates(Map<PlayerId, Player> players, GameState gameState ) {

        for (PlayerId playerID: PlayerId.ALL) {
            Player player = players.get(playerID);
            player.updateState(gameState, gameState.playerState(playerID));
        }                      
    }
}
