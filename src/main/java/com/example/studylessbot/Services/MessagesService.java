package com.example.studylessbot.Services;

import com.example.studylessbot.Entites.Message;
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

    public List<Message> getAllMessages() {
        return messagesRepos.findAll();
    }

    public boolean addMessage(Message message) {
        messagesRepos.save(message);
        return true;
    }


}
