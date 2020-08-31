package ru.nexgen.botnotifier.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nexgen.botnotifier.mapper.ChatsMapper;

import javax.annotation.PostConstruct;

/**
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbService {
    @Getter
    private final ChatsMapper chatsMapper;

    @PostConstruct
    public void init() {
        chatsMapper.createChatsTableIfNotExists();
    }
}
