package com.example.studylessbot.Controllers;

import com.example.studylessbot.Entites.ChatMessage;
import com.example.studylessbot.Entites.MessageType;
import com.example.studylessbot.Services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Controller
public class MainController {

    public static final Logger logger = Logger.getLogger(MainController.class.getName());
    private final MessagesService messageService;
    //private List<String> headers = List.of("Weekly task","Invitation","Poll","Presentation","Recording");
    private final Map<String, MessageType> headers = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("coins",MessageType.COINS) ,
            new AbstractMap.SimpleEntry<>("weeklytask",MessageType.WEEKLY_TASK) ,
            new AbstractMap.SimpleEntry<>("invitation",MessageType.INVITATION) ,
            new AbstractMap.SimpleEntry<>("presentation",MessageType.PRESENTATION) ,
            new AbstractMap.SimpleEntry<>("recording",MessageType.RECORDING),
            new AbstractMap.SimpleEntry<>("poll",MessageType.POLL)
    );


    @Autowired
    public MainController(MessagesService messageService) {

        logger.log(Level.INFO, "Initializing MainController");
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(@Nullable String groupName,@Nullable String from,@Nullable String to, Model model) {
            List<ChatMessage> messages = messageService.getAllMessages().stream().sorted(Comparator.comparing(ChatMessage::getDate)).toList();


        if(groupName!=null) {
            messages = messages.stream().filter(x->x.getGroupNumber().contains(groupName)).toList();
        }
        if(from!=null&&to!=null) {

            try{
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date fromDate = formatter.parse(from);
                Date toDate = formatter.parse(to);

                messages = messages.stream()
                        .filter(x->
                                x.getDateRaw().getTime()>=fromDate.getTime())
                        .filter(x->
                                x.getDateRaw().getTime()<=toDate.getTime())
                        .toList();
                model.addAttribute("from", from);
                model.addAttribute("to", to);
            }catch (Exception e){
                logger.log(Level.SEVERE, "Error while getting dates from " + from + " to " + to, e);
            }

        }

        Map<String, Map<String, List<ChatMessage>>> groupedMessages = messages.stream()
                .collect(Collectors.groupingBy(
                        ChatMessage::getGroupNumber, // Outer map key by groupName
                        Collectors.groupingBy( // Inner map key by date, grouping by message
                                ChatMessage::getDate // Group by date
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
