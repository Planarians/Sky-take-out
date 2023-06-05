package com.sky.task;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QuickTask {

    @Scheduled(cron = "0/5 * * * * *")
    public void showTime() {
        System.out.println(LocalDateTime.now());
    }
}
