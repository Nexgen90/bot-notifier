package ru.nexgen.botnotifier.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private Long chatId;
    private String chatName;
    private ZonedDateTime startTime;
    private ZonedDateTime lastCallTime;
    private ZonedDateTime stopTime;
    private ZonedDateTime banTime;
    private boolean isActive;
}
