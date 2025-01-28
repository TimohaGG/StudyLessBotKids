package com.example.studylessbot.Repos;

import com.example.studylessbot.Entites.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGroupsRepos extends JpaRepository<ChatGroup,Long> {
}
