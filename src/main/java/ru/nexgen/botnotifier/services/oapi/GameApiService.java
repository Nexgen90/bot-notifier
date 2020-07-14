package ru.nexgen.botnotifier.services.oapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by nikolay.mikutskiy
 * Date: 14.07.2020
 */
@Slf4j
@Service
public class GameApiService {
    public boolean isGamer(int userId) {
        //todo: http запрос к игровому серверу
        return false;
    }
}
