package com.example.studylessbot.Entites;

import jakarta.persistence.*;
import org.glassfish.jersey.server.wadl.WadlGenerator;

import java.util.List;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Teacher() {
    }

    public Teacher(Long id, String name, List<ChatGroup> groups) {
        this.id = id;
        this.name = name;
        this.groups = groups;
    }

    @OneToMany(mappedBy = "teacher")
    private List<ChatGroup> groups;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChatGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ChatGroup> groups) {
        this.groups = groups;
    }
}
