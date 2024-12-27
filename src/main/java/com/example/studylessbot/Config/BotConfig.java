package com.example.studylessbot.Config;

import com.example.studylessbot.Entites.Bot;
import com.example.studylessbot.Services.MessagesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

        MessagesService messagesService;
        public BotConfig(MessagesService messagesService) {
            this.messagesService = messagesService;
        }

        @Bean
        public TelegramBotsApi telegramBotsApi() {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                // Registering the bot with Spring context
                telegramBotsApi.registerBot(new Bot(messagesService));
                return telegramBotsApi;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

