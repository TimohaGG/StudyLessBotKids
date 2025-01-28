package com.example.studylessbot.Entites;

import jakarta.persistence.*;

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
    @ManyToOne
    private ChatGroup group;

    public ChatMessage() {

    }

    public ChatGroup getGroup() {
        return group;
    }

    public void setGroup(ChatGroup group) {
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public ChatMessage(MessageType messageType, Date date, ChatGroup group) {
        this.messageType = messageType;
        this.date = date;
        this.group = group;
    }


    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//    public void setGroupNumber(String groupNumber) {
//        this.groupNumber = groupNumber;
//    }

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
//    public String getGroupNumber() {
//        return groupNumber;
//    }

    public static String getFormatedDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd/MM/yyyy");
        return formatter.format(date);
    }


}
