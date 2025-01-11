package com.example.studylessbot.Services;

import com.example.studylessbot.Entites.ChatMessage;
import com.example.studylessbot.Repos.MessagesRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {
    @Autowired
    private MessagesRepos messagesRepos;
    public MessagesService(MessagesRepos messagesRepos) {
        this.messagesRepos = messagesRepos;
    }

    public List<ChatMessage> getAllMessages() {
        return messagesRepos.findAll();
    }

    public ChatMessage addMessage(ChatMessage message) {
        return messagesRepos.save(message);
    }

    public void addAllMessages(List<ChatMessage> messages) {
        messagesRepos.saveAll(messages);
    }


}
