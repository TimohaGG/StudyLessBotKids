package com.example.studylessbot.Entites;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable=false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;

    public ChatGroup() {
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public ChatGroup(long id, String name, List<ChatMessage> chatMessages, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.chatMessages = chatMessages;
        this.teacher = teacher;

    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}
