package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */

@Slf4j
@Service
public class MapLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException {

        File file = new File("/Users/nikolay.mikutskiy/git/bot-notifier/src/main/resources/game/map/map.json");
        Map<Integer, Location> map = objectMapper.readValue(file, new TypeReference<HashMap<Integer, Location>>() {
        });
        log.info("Map is loaded: {} locations", map.size());
    }

}
