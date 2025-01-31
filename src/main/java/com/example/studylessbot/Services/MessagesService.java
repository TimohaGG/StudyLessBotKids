package com.example.studylessbot.Services;

import com.example.studylessbot.Entites.ChatMessage;
import com.example.studylessbot.Entites.ChatGroup;
import com.example.studylessbot.Entites.Teacher;
import com.example.studylessbot.Repos.IGroupsRepos;
import com.example.studylessbot.Repos.MessagesRepos;
import com.example.studylessbot.Repos.iTeacherRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    private final MessagesRepos messagesRepos;
    private final IGroupsRepos iGroupsRepos;
    private final iTeacherRepos iTeacherRepos;
    @Autowired
    public MessagesService(MessagesRepos messagesRepos, IGroupsRepos iGroupsRepos, iTeacherRepos iTeacherRepos) {
        this.messagesRepos = messagesRepos;
        this.iGroupsRepos = iGroupsRepos;
        this.iTeacherRepos = iTeacherRepos;
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

    public Teacher getTeacher(String teacherName) {
        return iTeacherRepos.findAll().stream().filter(x->x.getName().equals(teacherName)).findFirst().orElse(null);
    }

    public void saveTeacher(Teacher teacher) {
        iTeacherRepos.save(teacher);
    }

    public List<String> getTeachersNames(){
        return iTeacherRepos.findAll().stream().map(Teacher::getName).toList();
    }

    public Teacher getDefaultTeacher(){
        return iTeacherRepos.findAll().stream().filter(x->x.getName().equals("No Teacher")).findFirst().orElse(null);
    }
}
