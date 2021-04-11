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
	
	public static final String TOPIC_CHAT 			= "/chat";
	public static final String TOPIC_PETICION 		= "/peticion";
	public static final String TOPIC_INVITACION 	= "/invitacion";
	public static final String TOPIC_BUSQUEDA 		= "/busqueda";
	public static final String TOPIC_PARTIDA_ACT 	= "/partida-act";
	public static final String TOPIC_PARTIDA_CHAT 	= "/partida-chat";
	public static final String TOPIC_PARTIDA_COM 	= "/partida-com";
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		
		//Broadcast communications
		config.enableSimpleBroker(	TOPIC_CHAT,
									TOPIC_PETICION,
									TOPIC_INVITACION,
									TOPIC_BUSQUEDA,
									TOPIC_PARTIDA_ACT,
									TOPIC_PARTIDA_CHAT
								);
		
		//Application calls
		config.setApplicationDestinationPrefixes("/app");
	}
		
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(ENDPOINT).withSockJS();
	}

}