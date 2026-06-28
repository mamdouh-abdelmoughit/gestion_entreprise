package com.btp.service;

import com.btp.dto.MessageDTO;
import com.btp.entity.Message;
import com.btp.entity.Projet;
import com.btp.entity.User;
import com.btp.exception.ResourceNotFoundException;
import com.btp.repository.MessageRepository;
import com.btp.repository.ProjetRepository;
import com.btp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {

    @Autowired private MessageRepository messageRepository;
    @Autowired private ProjetRepository projetRepository;
    @Autowired private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MessageDTO> getHistory(Long projetId, int limit) {
        return messageRepository
                .findByProjetIdOrderByTimestampDesc(projetId, PageRequest.of(0, limit))
                .stream()
                .map(this::toDTO)
                // Reverse to chronological order for display
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> { java.util.Collections.reverse(list); return list; }
                ));
    }

    /**
     * Called from WebSocket controller — resolves sender from the STOMP principal
     * (the authenticated user set by WebSocketAuthInterceptor).
     */
    public MessageDTO saveFromPrincipal(MessageDTO dto, Principal principal) {
        User expediteur = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + principal.getName()));

        Message entity = new Message();
        entity.setContenu(dto.getContenu());
        entity.setTimestamp(Instant.now());
        entity.setExpediteur(expediteur);

        Projet projet = projetRepository.findById(dto.getProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + dto.getProjetId()));
        entity.setProjet(projet);

        String roleDisplay = expediteur.getRoles().stream()
                .map(r -> r.getNom())
                .map(MessageService::roleToLabel)
                .findFirst()
                .orElse("USER");
        entity.setRoleDisplay(roleDisplay);

        return toDTO(messageRepository.save(entity));
    }

    public MessageDTO save(MessageDTO dto) {
        Message entity = new Message();
        entity.setContenu(dto.getContenu());
        entity.setTimestamp(Instant.now());

        User expediteur = userRepository.findById(dto.getExpediteurId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getExpediteurId()));
        entity.setExpediteur(expediteur);

        Projet projet = projetRepository.findById(dto.getProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found: " + dto.getProjetId()));
        entity.setProjet(projet);

        // Derive roleDisplay from user's primary role
        String roleDisplay = expediteur.getRoles().stream()
                .map(r -> r.getNom())
                .map(MessageService::roleToLabel)
                .findFirst()
                .orElse("USER");
        entity.setRoleDisplay(roleDisplay);

        return toDTO(messageRepository.save(entity));
    }

    private static String roleToLabel(String roleName) {
        return switch (roleName) {
            case "ROLE_ADMIN"      -> "DG";
            case "ROLE_CP"         -> "CP";
            case "ROLE_CC"         -> "CC";
            case "ROLE_CLIENT"     -> "CLIENT";
            case "ROLE_FOURNISSEUR"-> "FOURNISSEUR";
            default                -> "EMPLOYEE";
        };
    }

    private MessageDTO toDTO(Message e) {
        MessageDTO dto = new MessageDTO();
        dto.setId(e.getId());
        dto.setContenu(e.getContenu());
        dto.setTimestamp(e.getTimestamp());
        dto.setRoleDisplay(e.getRoleDisplay());
        if (e.getExpediteur() != null) {
            dto.setExpediteurId(e.getExpediteur().getId());
            dto.setExpediteurUsername(e.getExpediteur().getUsername());
            String nom = ((e.getExpediteur().getFirstName() != null ? e.getExpediteur().getFirstName() : "") +
                    " " + (e.getExpediteur().getLastName() != null ? e.getExpediteur().getLastName() : "")).trim();
            dto.setExpediteurNomComplet(nom.isEmpty() ? e.getExpediteur().getUsername() : nom);
        }
        if (e.getProjet() != null) {
            dto.setProjetId(e.getProjet().getId());
            dto.setProjetNom(e.getProjet().getNom());
        }
        return dto;
    }
}
