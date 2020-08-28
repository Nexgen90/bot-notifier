package ru.nexgen.botnotifier.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String startTime;
    private String lastCallTime;
    private String stopTime;
    private String banTime;
    private boolean isActive;
}
