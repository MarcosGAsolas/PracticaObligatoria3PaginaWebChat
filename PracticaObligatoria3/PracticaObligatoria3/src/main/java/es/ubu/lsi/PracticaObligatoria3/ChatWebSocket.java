package es.ubu.lsi.PracticaObligatoria3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatWebSocket extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper para convertir JSON a objetos Java

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Lógica después de que se establezca la conexión
        System.out.println("Nueva conexión establecida: " + session.getId());
        sessions.add(session); // Agregar la sesión a la lista de sesiones activas
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        // Lógica para manejar mensajes recibidos
        String payload = textMessage.getPayload();
        System.out.println("Mensaje recibido: " + payload);

        // Convertir el mensaje JSON a objeto Message
        Message message = objectMapper.readValue(payload, Message.class);

        // Obtener el nombre de usuario desde el encabezado
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap((org.springframework.messaging.Message<?>) session);
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            message.setFrom(username); // Establecer el nombre de usuario en el mensaje
        } else {
            System.err.println("Error: No se encontró el nombre de usuario en la sesión.");
            return; // Opcional: Manejar el error si no se encuentra el nombre de usuario
        }

        // Enviar el mensaje a todos los clientes conectados
        sendMessageToAll(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Lógica después de que se cierre la conexión
        System.out.println("Conexión cerrada: " + session.getId() + ", código de cierre: " + status.getCode());
        sessions.remove(session); // Quitar la sesión de la lista de sesiones activas
    }

    private void sendMessageToAll(Message message) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(message));
        for (WebSocketSession session : sessions) {
            session.sendMessage(textMessage);
        }
    }
}