package com.example.studylessbot.Entites;

import com.example.studylessbot.Controllers.MainController;
import com.example.studylessbot.Services.MessagesService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot extends TelegramLongPollingBot {

    private final Map<String,MessageType> tags = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("coins",MessageType.COINS) ,
            new AbstractMap.SimpleEntry<>("weeklytask",MessageType.WEEKLY_TASK) ,
            new AbstractMap.SimpleEntry<>("invitation",MessageType.INVITATION) ,
            new AbstractMap.SimpleEntry<>("presentation",MessageType.PRESENTATION) ,
            new AbstractMap.SimpleEntry<>("recording",MessageType.RECORDING),
            new AbstractMap.SimpleEntry<>("poll",MessageType.POLL)
    );

    MessagesService messagesService;

    public Bot() {}


    public Bot(MessagesService messagesService) {
        this.messagesService = messagesService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        try{

            if(!update.hasMessage()){
                return;
            }

            Message message = update.getMessage();
            String chatName = extractChat(message.getChat().getTitle());
            List<ChatMessage> msgs = new ArrayList<>();
            ChatMessage tmp = new ChatMessage();
            tmp.setDate(new Date((long    )message.getDate()*1000));
            tmp.setGroupNumber(chatName);
            if(message.hasPoll()){
                tmp.setMessageType(MessageType.POLL);
                msgs.add(tmp);
                MainController.logger.log(Level.INFO,"Poll added!");
            }
            if(message.hasText()||message.hasPhoto()||message.hasDocument()||message.hasVideo())
            {
                String text = message.hasText()?message.getText():message.getCaption();
                Pattern pattern = Pattern.compile("#(\\w+)");
                Matcher matcher = pattern.matcher(text);
                while(matcher.find()){
                    if(tags.containsKey(matcher.group(1))){
                        tmp.setMessageType(tags.get(matcher.group(1)));
                        msgs.add(tmp);
                        MainController.logger.log(Level.INFO,"Added tag: " + matcher.group(1));
                    }

                }


            }
            for (ChatMessage chatMessage : msgs) {
                ChatMessage res = messagesService.addMessage(chatMessage);
                if(res==null){
                    MainController.logger.log(Level.SEVERE,"Error while adding chat message: " + chatMessage.getMessageType());
                }
            }


        }catch (Exception e){
           MainController.logger.log(Level.WARNING, e.getMessage(),e);

        }


    }

    private String extractChat(String chatName){
        Pattern pattern = Pattern.compile("\\,(\\d+)");
        Matcher matcher = pattern.matcher(chatName);
        if(matcher.find()) {
            chatName = matcher.group(1);
        }
        else{
            pattern = Pattern.compile("група\\s*(\\d+)");
            matcher = pattern.matcher(chatName);
            if(matcher.find()) {
                chatName = matcher.group(1);
            }
            else{
                pattern = Pattern.compile("SL KIDS\\s*(\\d+)");
                matcher = pattern.matcher(chatName);
                if(matcher.find()) {
                    chatName = matcher.group(1);
                }
            }

        }
        return chatName;
    }


    @Override
    public String getBotUsername() {
        // TODO
        return "studyless_test_bot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "7712861655:AAGsdpMocRxgtaNVUqCkE8XS7pP92dbTKyc";
    }



}