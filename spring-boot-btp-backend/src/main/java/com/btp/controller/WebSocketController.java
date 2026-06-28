package com.btp.controller;

import com.btp.dto.MessageDTO;
import com.btp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    @Autowired private MessageService messageService;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    /**
     * Client sends to: /app/projet/{projetId}/send
     * Broadcast to:    /topic/projet/{projetId}
     *
     * The message is persisted to DB before broadcast so history is always consistent.
     */
    @MessageMapping("/projet/{projetId}/send")
    public void sendMessage(
            @DestinationVariable Long projetId,
            @Payload MessageDTO dto,
            Principal principal) {

        // Ensure the projetId from the destination is used (not a spoofed payload value)
        dto.setProjetId(projetId);

        // Resolve sender from the authenticated WebSocket principal
        if (principal != null && dto.getExpediteurId() == null) {
            // The service will resolve the User from the JWT-authenticated principal
            dto.setExpediteurUsername(principal.getName());
        }

        MessageDTO saved = messageService.saveFromPrincipal(dto, principal);
        messagingTemplate.convertAndSend("/topic/projet/" + projetId, saved);
    }
}
