package es.ubu.lsi.PracticaObligatoria3;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // Habilitar un simple broker para enviar mensajes a los clientes
        config.setApplicationDestinationPrefixes("/app");  // Prefijo para los mensajes enviados desde los clientes al servidor
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat.addUser")  // Endpoint al que se conectan los clientes WebSocket
                .setAllowedOrigins("\"http://localhost:8080\"")  // conexion desde la que se lanza el tomcat
                .withSockJS();  // Habilitar soporte SockJS para la comunicación con navegadores que no soportan WebSocket puro
    }
}