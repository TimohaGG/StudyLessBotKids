package com.example.studylessbot.Repos;

import com.example.studylessbot.Entites.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepos extends JpaRepository<Message,Long> {
}
