package com.example.studylessbot.Controllers;

import com.example.studylessbot.Entites.Bot;
import com.example.studylessbot.Entites.Message;
import com.example.studylessbot.Entites.MessageType;
import com.example.studylessbot.Services.MessagesService;
import com.mysql.cj.protocol.MessageSender;
import jakarta.ws.rs.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class MainController {

    private MessagesService messageService;
    //private List<String> headers = List.of("Weekly task","Invitation","Poll","Presentation","Recording");
    private Map<String, MessageType> headers = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("coins",MessageType.COINS) ,
            new AbstractMap.SimpleEntry<>("weeklytask",MessageType.WEEKLY_TASK) ,
            new AbstractMap.SimpleEntry<>("invitation",MessageType.INVITATION) ,
            new AbstractMap.SimpleEntry<>("presentation",MessageType.PRESENTATION) ,
            new AbstractMap.SimpleEntry<>("recording",MessageType.RECORDING),
            new AbstractMap.SimpleEntry<>("poll",MessageType.POLL)
    );


    @Autowired
    public MainController(MessagesService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(@Nullable String groupName, Model model) {
            List<Message> messages = messageService.getAllMessages().stream().sorted(Comparator.comparing(Message::getDate)).toList();
        messages = messages.stream()
                .filter(x->x.getDateRaw().getMonth()==new Date().getMonth())
                .toList();

        if(groupName!=null) {
            messages = messages.stream().filter(x->x.getGroupNumber().contains(groupName)).toList();
        }

        Map<String, Map<String, List<Message>>> groupedMessages = messages.stream()
                .collect(Collectors.groupingBy(
                        Message::getGroupNumber, // Outer map key by groupName
                        Collectors.groupingBy( // Inner map key by date, grouping by message
                                Message::getDate // Group by date
                        )
                ));



        Map<String, Map<String, List<Boolean>>> res = groupedMessages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Group key (groupNumber)
                        groupEntry -> groupEntry.getValue().entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey, // Date key
                                        dateEntry -> headers.keySet().stream()
                                                .map(type -> dateEntry.getValue().stream()
                                                        .anyMatch(message -> message.getMessageType().equals(headers.get(type)))) // Check if any message matches the type
                                                .collect(Collectors.toList()) // Collect booleans into a List
                                ))
                ));

        model.addAttribute("messages", res);
        model.addAttribute("headers", headers);
        model.addAttribute("groupSearch", groupName);
        return "index";
    }
}
