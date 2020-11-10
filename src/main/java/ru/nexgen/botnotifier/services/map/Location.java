package ru.nexgen.botnotifier.services.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 31.08.2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private Integer id;
    private MapType type;
    private String code;
    private String name;
    private String smallMapImageId;
    private String fullMapImageId;

    private List<Integer> nextLocationsId;

}
