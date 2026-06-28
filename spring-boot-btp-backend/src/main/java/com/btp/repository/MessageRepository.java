package com.btp.repository;

import com.btp.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Returns last N messages for a project, ordered oldest-first for chat display
    List<Message> findByProjetIdOrderByTimestampAsc(Long projetId);
    Page<Message> findByProjetIdOrderByTimestampDesc(Long projetId, Pageable pageable);
}
