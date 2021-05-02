package es.susangames.catan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class ws {

    static {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<Transport>(); 
        transports.add(new WebSocketTransport(webSocketClient)); 
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession session;
        try {
            session = stompClient
                    .connect(
                        "http://localhost:8080/catan-stomp-ws-ep", 
                        new StompSessionHandlerAdapter() {})
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Map<String,String> map = new HashMap<String,String>(); 
        map.put("from", "f"); 
        map.put("to", "f");
        map.put("body", "Greeting sir!"); 
        
        session.subscribe("/chat/f", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println(payload);
            }
        });
        for(int i = 0;i < 1000000; ++i) {}
        session.send("/app/enviar/privado", new JSONObject(map).toString()); 
    }

    public static void initialize () {}

}