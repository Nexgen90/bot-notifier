package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nexgen.botnotifier.configuration.properties.GameLogicProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MapLoaderService {
    private final GameLogicProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<Integer, Location> loadAndGetGameMap() {
        Map<Integer, Location> map = new HashMap<>();
        log.info("Start loading game map");
        List<File> files = listFilesForFolder();

        files.forEach(file -> {
            try {
                map.putAll(objectMapper.readValue(file, new TypeReference<HashMap<Integer, Location>>() {
                }));
            } catch (IOException e) {
                log.error("Error while loading map files: {}", file.getName(), e);
            }
        });

        log.info("Map is loaded: {} locations from {} files", map.size(), files.size());
        return map;
    }

    private List<File> listFilesForFolder() {
        File folder = new File(properties.getMapsFolderPath());
        if (!folder.exists()) {
            log.error("Can't load game map: folder {} does not exist!", properties.getMapsFolderPath());
            return Collections.emptyList();
        }
        List<File> mapFiles = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                log.info("Detected map file: {}", fileEntry.getName());
                mapFiles.add(fileEntry);
            }
        }
        return mapFiles;
    }
}
