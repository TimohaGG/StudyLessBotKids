package com.example.studylessbot.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private long id;

    private MessageType messageType;
    private Date date;
    private String groupNumber;


    public ChatMessage() {

    }

    public long getId() {
        return id;
    }

    public ChatMessage(MessageType messageType, Date date, String groupNumber) {
        this.messageType = messageType;
        this.date = date;
        this.groupNumber = groupNumber;
    }


    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        return formatter.format(date) + " " + day.format(date);
    }

    public Date getDateRaw(){
        return date;
    }

    public Date getDateOnly(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set the time to midnight (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    public String getGroupNumber() {
        return groupNumber;
    }

    public static String getFormatedDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
        return formatter.format(date);
    }


}
