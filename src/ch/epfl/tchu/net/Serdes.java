package ch.epfl.tchu.net;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Player.TurnKind;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicCardState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.PublicPlayerState;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * Contient la totalité des serdes utiles au projet.
 * 
 * @author Ilias Marwane Merigh (330316)
 */

// non instanciable = final ?

public class Serdes {

	private Serdes() {}
	
	/**
	 * Serde associé au type Integer.
	 */
	public static final Serde<Integer> INTEGER = Serde.of(
		    i -> Integer.toString(i),
		    Integer::parseInt);

	/**
	 * Serde associé au type String.
	 */
	private static Function<String, String> clearToBase16 = new Function<String, String>() {
		   @Override
		   public String apply(String s) {
				return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));		
		   }
	};
		   
	private static Function<String, String> base16ToClear = new Function<String, String>() {
		   @Override
		   public String apply(String s) {
				return new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)));		
		   }
	};
		   	
	public static final Serde<String> STRING = Serde.of(clearToBase16, base16ToClear);

	public static final Serde<List<String>> STRING_LIST = Serde.listOf(STRING, ",");	
	
	/**
	 * Serde associé au type PlayerId.
	 */
	public static final Serde<PlayerId> PLAYERID = Serde.oneOf(PlayerId.ALL);

	/**
	 * Serde associé au type TurnKind.
	 */
	public static final Serde<TurnKind> TURNKIND = Serde.oneOf(TurnKind.ALL);


	/**
	 * Card
	 */
	public static final Serde<Card> CARD = Serde.oneOf(Card.ALL);
	public static final Serde<List<Card>> CARD_LIST = Serde.listOf(CARD, ",");	
	public static final Serde<SortedBag<Card>> CARD_SORTED_BAG = Serde.bagOf(CARD, ",");	
	public static final Serde<List<SortedBag<Card>>> CARD_SORTED_BAG_LIST = Serde.listOf(CARD_SORTED_BAG, ";");	

	
	/**
	 * Route
	 */
	public static final Serde<Route> ROUTE = Serde.oneOf(ChMap.routes());
	public static final Serde<List<Route>> ROUTE_LIST = Serde.listOf(ROUTE, ",");	

	
	/**
	 * Ticket
	 */
	public static final Serde<Ticket> TICKET = Serde.oneOf(ChMap.tickets());
	public static final Serde<SortedBag<Ticket>> TICKET_SORTED_BAG = Serde.bagOf(TICKET, ",");	
	

	
	/**
	 * PublicCardState
	 */

	private static Function<PublicCardState, String> pcsSerialize = new Function<PublicCardState, String>() {
		   @Override
		   public String apply(PublicCardState pcs) {
			   String s1 = CARD_LIST.serialize(pcs.faceUpCards());
			   String s2 = INTEGER.serialize(pcs.deckSize());
			   String s3 = INTEGER.serialize(pcs.discardsSize());
			   return s1 + ";" + s2 + ";" + s3;
		   }
	};
		   
	private static Function<String, PublicCardState> pcsDeserialize = new Function<String, PublicCardState>() {
		   @Override
		   public PublicCardState apply(String s) {
			   String[] sa = s.split(Pattern.quote(";"));
			   List<Card> faceUpCards = CARD_LIST.deserialize(sa[0]);
			   Integer deckSize = INTEGER.deserialize(sa[0]);
			   Integer discardsSize = INTEGER.deserialize(sa[0]);
			   
			   return new PublicCardState(faceUpCards, deckSize, discardsSize);
			   
			}
	};
	
	public static final Serde<PublicCardState> PUBLIC_CARD_STATE = Serde.of(pcsSerialize, pcsDeserialize);

	
	/**
	 * PublicPlayerState
	 */

	private static Function<PublicPlayerState, String> ppsSerialize = new Function<PublicPlayerState, String>() {
		   @Override
		   public String apply(PublicPlayerState pps) {
			   String s1 = INTEGER.serialize(pps.ticketCount());
			   String s2 = INTEGER.serialize(pps.cardCount());
			   String s3 = ROUTE_LIST.serialize(pps.routes());
			   return s1 + ";" + s2 + ";" + s3;
		   }
	};
		   
	private static Function<String, PublicPlayerState> ppsDeserialize = new Function<String, PublicPlayerState>() {
		   @Override
		   public PublicPlayerState apply(String s) {
			   String[] sa = s.split(Pattern.quote(";"));
			   Integer ticketCount = INTEGER.deserialize(sa[0]);
			   Integer cardCount = INTEGER.deserialize(sa[0]);
			   List<Route> routes = ROUTE_LIST.deserialize(sa[0]);
			   
			   return new PublicPlayerState(ticketCount, cardCount, routes);
			   
			}
	};
	
	public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE = Serde.of(ppsSerialize, ppsDeserialize);
	

	
	
	/**
	 * PlayerState
	 */

	private static Function<PlayerState, String> psSerialize = new Function<PlayerState, String>() {
		   @Override
		   public String apply(PlayerState ps) {
			   String s1 = TICKET_SORTED_BAG.serialize(ps.tickets());
			   String s2 = CARD_SORTED_BAG.serialize(ps.cards());
			   String s3 = ROUTE_LIST.serialize(ps.routes());
			   return s1 + ";" + s2 + ";" + s3;
		   }
	};
		   
	private static Function<String, PlayerState> psDeserialize = new Function<String, PlayerState>() {
		   @Override
		   public PlayerState apply(String s) {
			   String[] sa = s.split(Pattern.quote(";"));
			   SortedBag<Ticket> tickets = TICKET_SORTED_BAG.deserialize(sa[0]);
			   SortedBag<Card> cards = CARD_SORTED_BAG.deserialize(sa[0]);
			   List<Route> routes = ROUTE_LIST.deserialize(sa[0]);
			   
			   return new PlayerState(tickets, cards, routes);
			   
			}
	};
	
	public static final Serde<PlayerState> PLAYER_STATE = Serde.of(psSerialize, psDeserialize);
		

	
	
	
	/**
	 * PublicGameState
	 */

	private static Function<PublicGameState, String> pgsSerialize = new Function<PublicGameState, String>() {
		   @Override
		   public String apply(PublicGameState pgs) {
			   String s0 = INTEGER.serialize(pgs.ticketsCount());
			   String s1 = PUBLIC_CARD_STATE.serialize(pgs.cardState());
			   String s2 = PLAYERID.serialize(pgs.currentPlayerId());
			   String s3 = PUBLIC_PLAYER_STATE.serialize(pgs.playerState(PlayerId.PLAYER_1));
			   String s4 = PUBLIC_PLAYER_STATE.serialize(pgs.playerState(PlayerId.PLAYER_2));
			   String s5 = PLAYERID.serialize(pgs.lastPlayer());

			   return s0 + ":" + s1 + ":" + s2 + ":" + s3 + ":" + s4 + ":" + s5;
		   }
	};
		   
	private static Function<String, PublicGameState> pgsDeserialize = new Function<String, PublicGameState>() {
		   @Override
		   public PublicGameState apply(String s) {
			   String[] sa = s.split(Pattern.quote(":"));

			   Integer ticketsCount = INTEGER.deserialize(sa[0]);
			   PublicCardState cardState = PUBLIC_CARD_STATE.deserialize(sa[1]);
			   PlayerId currentPlayerId = PLAYERID.deserialize(sa[2]);
			   PublicPlayerState playerState_1 = PUBLIC_PLAYER_STATE.deserialize(sa[3]);
			   PublicPlayerState playerState_2 = PUBLIC_PLAYER_STATE.deserialize(sa[4]);
			   PlayerId lastPlayer = PLAYERID.deserialize(sa[5]);
			   
				Map<PlayerId, PublicPlayerState> publicPlayerState = new EnumMap<>(PlayerId.class);
				publicPlayerState.put(PlayerId.PLAYER_1, playerState_1);
				publicPlayerState.put(PlayerId.PLAYER_2, playerState_2);
		   
			   return new PublicGameState(ticketsCount, cardState, currentPlayerId, publicPlayerState, lastPlayer);
			   
			}
	};
	
	public static final Serde<PublicGameState> PUBLIC_GAME_STATE = Serde.of(pgsSerialize, pgsDeserialize);
}
