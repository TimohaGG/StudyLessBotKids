package com.example.studylessbot.Services;

import com.example.studylessbot.Entites.ChatMessage;
import com.example.studylessbot.Entites.ChatGroup;
import com.example.studylessbot.Repos.IGroupsRepos;
import com.example.studylessbot.Repos.MessagesRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {

    private final MessagesRepos messagesRepos;
    private final IGroupsRepos iGroupsRepos;
    @Autowired
    public MessagesService(MessagesRepos messagesRepos, IGroupsRepos iGroupsRepos) {
        this.messagesRepos = messagesRepos;
        this.iGroupsRepos = iGroupsRepos;
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

    public ChatGroup hasGroup(String groupName) {
        return iGroupsRepos.findAll().stream().filter(x->x.getName().equals(groupName)).findFirst().orElse(null);
    }

    public void saveGroup(ChatGroup group) {
        iGroupsRepos.save(group);
    }

    public List<ChatGroup> getAllGroups(){
        return iGroupsRepos.findAll();
    }
}
