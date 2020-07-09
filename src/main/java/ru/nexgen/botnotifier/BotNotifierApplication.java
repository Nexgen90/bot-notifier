package ru.nexgen.botnotifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BotNotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotNotifierApplication.class, args);
        log.info("BotNotifier has started");
    }

}
