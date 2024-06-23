package es.ubu.lsi.PracticaObligatoria3.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import es.ubu.lsi.PracticaObligatoria3.Message;

@Controller
public class WebSocketController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        // Obtener el nombre de usuario desde el encabezado
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        // Agregar el nombre de usuario al mensaje
        message.setFrom(username);
        // Devolver el mensaje modificado para ser enviado a todos los clientes
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        // Lógica para conectar usuarios y enviar mensajes de bienvenida
        headerAccessor.getSessionAttributes().put("username", message.getFrom());
        return message;
    }

    @MessageMapping("/chat.disconnect")
    @SendTo("/topic/public")
    public Message disconnectUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        // Lógica para desconectar al usuario
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        headerAccessor.getSessionAttributes().remove("username"); // Eliminar el nombre de usuario de la sesión
        message.setFrom(username);
        message.setText("Se ha desconectado."); // se agrega un mensaje de desconexion
        return message;
    }
}