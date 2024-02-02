package ch.epfl.tchu.net;

import java.util.List;


/**
 * Enumère les types de messages que le serveur peut envoyer aux clients. 
 * Ces messages correspondent directement aux méthodes de l'interface Player.
 * 
 * @author Ilias Marwane Merigh (330316)
 */
public enum MessageId {
	
	INIT_PLAYERS,
	RECEIVE_INFO,
	UPDATE_STATE,
	SET_INITIAL_TICKETS,
	CHOOSE_INITIAL_TICKETS,
	NEXT_TURN,
	CHOOSE_TICKETS,
	DRAW_SLOT,
	ROUTE,
	CARDS,
	CHOOSE_ADDITIONAL_CARDS;
	
	public static final List<MessageId> ALL = List.of(MessageId.values());
	
	public static final int COUNT = ALL.size();
}