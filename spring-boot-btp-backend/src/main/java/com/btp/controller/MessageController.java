package com.btp.controller;

import com.btp.dto.MessageDTO;
import com.btp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired private MessageService messageService;

    @GetMapping("/projet/{projetId}")
    @PreAuthorize("hasAnyRole('ADMIN','CP','CC','CLIENT')")
    public List<MessageDTO> getHistory(
            @PathVariable Long projetId,
            @RequestParam(defaultValue = "100") int limit) {
        return messageService.getHistory(projetId, limit);
    }
}
