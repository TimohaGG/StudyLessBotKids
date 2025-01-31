package com.example.studylessbot.Controllers;

import com.example.studylessbot.Entites.ChatGroup;
import com.example.studylessbot.Entites.ChatMessage;
import com.example.studylessbot.Entites.MessageType;
import com.example.studylessbot.Services.MessagesService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
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


    private double totalPages;
    private final long itemsPerPage = 10;

    @Autowired
    public MainController(MessagesService messageService) {

        logger.log(Level.INFO, "Initializing MainController");
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(@Nullable String groupName, @Nullable String from, @Nullable String to, @RequestParam(defaultValue = "0") long pageNum,@Nullable String teacher, Model model) {

        List<ChatGroup> groups = messageService.getAllGroups()
                .stream()
                .filter(x->!x.getChatMessages().isEmpty()).toList();

        if(teacher != null) {
            groups = groups.stream().filter(x->{
                if(x.getTeacher()!=null)
                    return x.getTeacher().getName().equals(teacher);
                else
                    return false;
            }).toList();
        }
        Date fromDate = null, toDate = null;
        if ( (from != null&&!from.isEmpty()) && (to != null&&!to.isEmpty())) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                fromDate = formatter.parse(from);
                toDate = formatter.parse(to);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date finalFromDate = fromDate;
            Date finalToDate = toDate;
            groups = groups.stream()
                    .filter(group -> group.getChatMessages().stream()
                            .anyMatch(msg -> {
                                Date messageDate = msg.getDateRaw();
                                return messageDate.compareTo(finalFromDate) >= 0 && messageDate.compareTo(finalToDate) <= 0;
                            }))
                    .toList();
        }


        totalPages = groups.size() < itemsPerPage ? 1 : Math.ceil((double) groups.size() /itemsPerPage);;


        groups = groups.stream()
                .skip(pageNum*itemsPerPage)
                .limit(itemsPerPage).toList();

        if(pageNum>totalPages || pageNum<0){
            return "redirect:/";
        }
        Map<ChatGroup,Map<String,List<Boolean>>> res =  groups.stream()
                // Step 1: Filter ChatGroups by name if provided
                .filter(group -> groupName == null || group.getName().contains(groupName))
                // Step 2: Map each ChatGroup to its structured data
                .collect(Collectors.toMap(
                        group -> group, // Key is the ChatGroup itself
                        group -> group.getChatMessages().stream()
                                // Step 3: Filter messages by date if provided

                                // Step 4: Group messages by formatted date
                                .collect(Collectors.groupingBy(
                                        ChatMessage::getDate, // Key is the formatted date as a String
                                        Collectors.collectingAndThen(
                                                Collectors.toList(), // Collect messages for each date
                                                msgList -> headers.keySet().stream()
                                                        .map(typeKey -> msgList.stream()
                                                                .anyMatch(message -> message.getMessageType().equals(headers.get(typeKey))))
                                                        .collect(Collectors.toList()) // Collect into List<Boolean>
                                        )
                                ))
                ));


        model.addAttribute("messages", res);
        model.addAttribute("headers", headers);

        model.addAttribute("groupSearch", groupName);
        model.addAttribute("from", from ==null ? "":from);
        model.addAttribute("to", to ==null ? "":to);

        model.addAttribute("teachers", messageService.getTeachersNames());

        model.addAttribute("totalPages", totalPages-1);
        model.addAttribute("prevPage", pageNum==0 ? 0:pageNum-1);
        model.addAttribute("nextPage", pageNum==totalPages-1 ? pageNum:pageNum+1);
        model.addAttribute("currentPage", pageNum);

        return "index";
    }
}
