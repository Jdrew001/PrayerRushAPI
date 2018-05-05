package com.dtatkison.prayerrush.rushapi.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 10000)
    public void sendGreetings() {
        System.out.println("Sending");
        Gson gson = new Gson();

        //template.convertAndSend("/push", gson.toJson(new Greeting("Sent to dtatkison@gmail.com")));
        //template.convertAndSendToUser("dtatkison@gmail.com","/push", gson.toJson(new Greeting("testing")));
    }

    public class Greeting
    {
        private String message;

        public Greeting()
        {

        }

        public Greeting(String message)
        {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

}
