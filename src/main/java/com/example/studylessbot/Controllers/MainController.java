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


    private long totalPages;
    private final long itemsPerPage = 10;

    @Autowired
    public MainController(MessagesService messageService) {

        logger.log(Level.INFO, "Initializing MainController");
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String index(@Nullable String groupName, @Nullable String from, @Nullable String to, @RequestParam(defaultValue = "0") long pageNum, Model model) {



//        List<ChatGroup> groups = messageService.getAllGroups()
//                .stream().skip(pageNum*itemsPerPage)
//                .limit(itemsPerPage).toList();


//
////
//        List<ChatMessage> messages = messageService.getAllMessages().stream()
//                .sorted(Comparator.comparing(ChatMessage::getDateRaw))
//                .skip(pageNum *itemsPerPage)
//                .limit(itemsPerPage)
//                .toList();
//
//
//        if(groupName!=null) {
//            groups = groups.stream().filter(x->x.getGroup().getName().contains(groupName)).toList();
//        }
//        if(from!=null&&to!=null) {
//
//            try{
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                Date fromDate = formatter.parse(from);
//                Date toDate = formatter.parse(to);
//
//                groups = groups.stream()
//                        .filter(x->
//                                x.getDateRaw().getTime()>=fromDate.getTime())
//                        .filter(x->
//                                x.getDateRaw().getTime()<=toDate.getTime())
//                        .toList();
//                model.addAttribute("from", from);
//                model.addAttribute("to", to);
//            }catch (Exception e){
//                logger.log(Level.SEVERE, "Error while getting dates from " + from + " to " + to, e);
//            }
//
//        }
//
//
//
//        Map<String, Map<String, List<ChatMessage>>> groupedMessages = groups.stream()
//                .collect(Collectors.groupingBy(
//                        msg->msg.getGroup().getName(), // Outer map key by groupName
//                        Collectors.groupingBy( // Inner map key by date, grouping by message
//                                ChatMessage::getDate // Group by date
//                        )
//                ));
//
//
//
//        Map<String, Map<String, List<Boolean>>> res = groupedMessages.entrySet().stream()
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey, // Group key (groupNumber)
//                        groupEntry -> groupEntry.getValue().entrySet().stream()
//                                .collect(Collectors.toMap(
//                                        Map.Entry::getKey, // Date key
//                                        dateEntry -> headers.keySet().stream()
//                                                .map(type -> dateEntry.getValue().stream()
//                                                        .anyMatch(message -> message.getMessageType().equals(headers.get(type)))) // Check if any message matches the type
//                                                .collect(Collectors.toList()) // Collect booleans into a List
//                                ))
//                ));

//--------------------------------------------------------------------
//
//        List<ChatMessage> messages = messageService.getAllMessages().stream()
//                .sorted(Comparator.comparing(ChatMessage::getDateRaw))
//                .skip(pageNum *itemsPerPage)
//                .limit(itemsPerPage)
//                .toList();
//        // Step 1: Sort messages by date and paginate
//        List<ChatMessage> filteredMessages = messages.stream()
//                .sorted(Comparator.comparing(ChatMessage::getDateRaw))
//                .skip(pageNum * itemsPerPage)
//                .limit(itemsPerPage)
//                .toList();
//
//        // Step 2: Filter by group name if provided
//        if (groupName != null) {
//            filteredMessages = filteredMessages.stream()
//                    .filter(msg -> msg.getGroup().getName().contains(groupName))
//                    .toList();
//        }
//
//        // Step 3: Filter by date range if provided
//        if (from != null && to != null) {
//            try {
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                Date fromDate = formatter.parse(from);
//                Date toDate = formatter.parse(to);
//
//                filteredMessages = filteredMessages.stream()
//                        .filter(msg -> msg.getDateRaw().getTime() >= fromDate.getTime())
//                        .filter(msg -> msg.getDateRaw().getTime() <= toDate.getTime())
//                        .toList();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Step 4: Group by ChatGroup and date, and convert to the desired structure
//        Map<ChatGroup, Map<String, boolean[]>> result = filteredMessages.stream()
//                .collect(Collectors.groupingBy(
//                        ChatMessage::getGroup, // Group by ChatGroup
//                        Collectors.groupingBy(
//                                msg -> msg.getDate(), // Group by formatted date as String
//                                Collectors.collectingAndThen(
//                                        Collectors.toList(), // Collect messages of the same group and date
//                                        msgList -> {
//                                            // Create a boolean[] for each date
//                                            Object[] typeArray = headers.keySet().stream()
//                                                    .map(typeKey -> msgList.stream()
//                                                            .anyMatch(message -> message.getMessageType()
//                                                                    .equals(headers.get(typeKey))))
//                                                    .toArray(); // Collect into Boolean[] first
//
//                                            // Convert Boolean[] to boolean[]
//                                            boolean[] primitiveArray = new boolean[typeArray.length];
//                                            for (int i = 0; i < typeArray.length; i++) {
//                                                primitiveArray[i] = Boolean.TRUE.equals(typeArray[i]);
//                                            }
//                                            return primitiveArray;
//                                        }
//                                )
//                        )
//                ));
        List<ChatGroup> groups = messageService.getAllGroups()
                .stream()
                .filter(x->!x.getChatMessages().isEmpty()).toList();


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
                                return messageDate.compareTo(finalFromDate) > 0 && messageDate.compareTo(finalToDate) < 0;
                            }))
                    .toList();
        }


        totalPages =  groups.size() < itemsPerPage ? 1 : Math.round((float) groups.size() / itemsPerPage);

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


        model.addAttribute("totalPages", totalPages-1);
        model.addAttribute("prevPage", pageNum==0 ? 0:pageNum-1);
        model.addAttribute("nextPage", pageNum==totalPages-1 ? pageNum:pageNum+1);
        model.addAttribute("currentPage", pageNum);

        return "index";
    }
}
