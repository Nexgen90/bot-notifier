package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */

@Slf4j
@Service
public class MapLoaderService {
    @Value("${game.map.mapsFolderPath:#{null}}")
    private String mapsFolderPath;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<File> files;

    public Map<Integer, Location> loadAndGetGameMap() {
        Map<Integer, Location> map = new HashMap<>();
        log.info("Start loading game map");
        File folder = new File(mapsFolderPath);
        if (!folder.exists()) {
            log.error("Can't load game map: folder {} does not exist!", mapsFolderPath);
            return map;
        }
        files = listFilesForFolder(folder);


        files.forEach(f -> {
            try {
                map.putAll(objectMapper.readValue(f, new TypeReference<HashMap<Integer, Location>>() {
                }));
            } catch (IOException e) {
                log.error("Error while loading map files: {}", f.getName(), e);
            }
        });

        log.info("Map is loaded: {} locations from {} files", map.size(), files.size());
        return map;
    }

    private List<File> listFilesForFolder(final File folder) {
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
