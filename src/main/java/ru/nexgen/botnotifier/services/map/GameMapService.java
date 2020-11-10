package ru.nexgen.botnotifier.services.map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.11.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameMapService {
    private final MapLoaderService loaderService;
    private final Map<Integer, Location> map = new HashMap<>();

    @PostConstruct
    void init() {
        map.putAll(loaderService.loadAndGetGameMap());
        log.debug("Loaded locations: {}", map);

    }

    public Location getLocationById(int id) {
        return map.get(id);
    }
}
