package es.susangames.catan;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private static final String ENDPOINT 			= "/catan-stomp-ws-ep";
	
	public static final String TOPIC_CHAT 			= "/chat";			// Subscribe("/chat/<playerId>")
	public static final String TOPIC_PETICION 		= "/peticion";		// Subscribe("/peticion/<playerId>")
	public static final String TOPIC_INVITACION 	= "/invitacion";	// Subscribe("/invitacion/<playerId>")
	public static final String TOPIC_SALA_CREAR 	= "/sala-crear";	// Subscribe("/sala-crear/<playerId>")
	public static final String TOPIC_SALA_ACT 		= "/sala-act";		// Subscribe("/sala-act/<roomId>")
	public static final String TOPIC_PARTIDA_ACT 	= "/partida-act";	// Subscribe("/partida-act/<gameId>")
	public static final String TOPIC_PARTIDA_CHAT 	= "/partida-chat";	// Subscribe("/partida-chat/<gameId>")
	public static final String TOPIC_PARTIDA_COM 	= "/partida-com";	// Subscribe("/partida-com/<gameId>/<playerId>")
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		
		//Broadcast communications
		config.enableSimpleBroker(	TOPIC_CHAT,
									TOPIC_PETICION,
									TOPIC_INVITACION,
									TOPIC_SALA_CREAR,
									TOPIC_SALA_ACT,
									TOPIC_PARTIDA_ACT,
									TOPIC_PARTIDA_CHAT,
									TOPIC_PARTIDA_COM
								);
		
		//Application calls
		config.setApplicationDestinationPrefixes("/app");
	}
		
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(ENDPOINT).withSockJS();
	}

}