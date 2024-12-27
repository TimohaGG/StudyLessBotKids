package com.example.studylessbot.Entites;

import com.example.studylessbot.Services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    private Map<String,MessageType> tags = Map.ofEntries(
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
        if (update.hasMessage() && (update.getMessage().hasText()||update.getMessage().hasPoll())) {
            Message message = update.getMessage();
            String chatName = message.getChat().getTitle(); // Get the chat ID of the message


            if(message.hasPoll()){
                com.example.studylessbot.Entites.Message messageToSave = new com.example.studylessbot.Entites.Message();
                messageToSave.setMessageType(MessageType.POLL);
                messageToSave.setDate(new Date((long    )message.getDate()*1000));
                messageToSave.setGroupNumber(chatName);
                messagesService.addMessage(messageToSave);
            }
            else{
                for (Map.Entry<String,MessageType> entry : tags.entrySet()) {
                    if(message.getText().contains("#"+entry.getKey())) {
                        com.example.studylessbot.Entites.Message messageToSave = new com.example.studylessbot.Entites.Message();
                        messageToSave.setMessageType(entry.getValue());

                        messageToSave.setDate(new Date((long    )message.getDate()*1000));
                        messageToSave.setGroupNumber(chatName);
                        messagesService.addMessage(messageToSave);
                    }

                }
            }

        }


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