package com.example.studylessbot.Repos;

import com.example.studylessbot.Entites.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepos extends JpaRepository<ChatMessage,Long> {
}
